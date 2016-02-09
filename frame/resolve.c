#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "frame.h"
#include "resolve.h"

#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"

#define NAME_MAX_LEN 50
#define BLK_MAX_LEN 5

lua_State* L;
int* blk_arr;
int blk_size = 0;
int blk_pos = 0;

void codec(FRM* frm)
{
    printf("resolve %s start!\n", frm->name);
    blk_arr = (int*) malloc(sizeof(int) * BLK_MAX_LEN);
    FILE* fp;
    char* package = "org.whut.mc.server.core.util";
    char* prefix = "../src/org/whut/mc/server/core/util/";
    char* suffix = ".java";
    char* name = (char*) malloc(sizeof(char) * (strlen(prefix) + strlen(suffix) + NAME_MAX_LEN));

    L = lua_open();
    luaL_openlibs(L);
    luaL_dofile(L, "script.lua");

    strcat(name, prefix);
    strcat(name, frm->name);
    strcat(name, suffix);

    FILE* fp_resolve = NULL;
    fp_resolve = fopen(name, "w");
    fprintf(fp_resolve, "package %s;\n\n", package);
    fprintf(fp_resolve, "import org.json.*;\n");
    fprintf(fp_resolve, "import org.whut.mc.server.core.communication.CodecBase;\n");
    fprintf(fp_resolve, "import java.io.UnsupportedEncodingException;\n\n");
    fprintf(fp_resolve, "public class %s extends CodecBase {\n\n", frm->name);
    fprintf(fp_resolve, "\tpublic String resolve(byte[] data) throws UnsupportedEncodingException {\n");
    fprintf(fp_resolve, "\t\tString json = \"\";\n");

    blk_gen(frm, 1);
    code_gen(fp_resolve, frm);
    char* prefix_blk = create_blk(blk_arr[blk_size - 1]);
    fprintf(fp_resolve, "\t\tjson += \"%s\";\n", prefix_blk);
    fprintf(fp_resolve, "\t\treturn json;\n");
    fprintf(fp_resolve, "\t}\n");


    fprintf(fp_resolve, "\tpublic byte[] code(JSONObject jsonObject) throws UnsupportedEncodingException {\n");
    fprintf(fp_resolve, "\t\tbyte[] bt = new byte[0];\n");
    byte_gen(fp_resolve, frm);
    fprintf(fp_resolve, "\t\treturn bt;\n");
    fprintf(fp_resolve, "\t}\n");
    fprintf(fp_resolve, "}\n");
    fclose(fp_resolve);
}

void blk_gen(FRM* frm, int blk)
{
    FLD_FRM* fld_frm = frm->fld_frm;
    int size = frm->fld_size;
    int count = 0;
    FLD* fld = NULL;
    for (; count < size; count++)
    {
        fld = fld_frm->fld;
        //lua_code_gen(fp, fld->name, fld->type, fld->size, fld->offset, fld->method);
        fld_frm = fld_frm->next;
    }
    blk_arr[blk_size++] = blk;
    blk_gen_sub(frm, ++blk);
    return;
}

void blk_gen_sub(FRM* frm, int blk)
{
    FRM* tmp = frm->child;
    int pos = 0;
    int tmp_blk;
    while (pos < frm->child_size)
    {
        blk_gen(tmp, blk);
        tmp++;
        pos++;
    }
}

void code_gen(FILE* fp, FRM* frm)
{
    printf("code gen : %s\n", frm->name);
    FLD_FRM* fld_frm = frm->fld_frm;
    int size = frm->fld_size;
    int count = 0;
    FLD* fld = NULL;

    if (blk_pos == 0)
    {
        fprintf(fp, "\t\tjson += \"{\";\n");
        blk_pos++;
    }
    else if (blk_pos != 0 && blk_pos < blk_size)
    {
        if (blk_arr[blk_pos] > blk_arr[blk_pos - 1])
        {
            fprintf(fp, "\t\tjson += \"\\\"%s\\\":{\";\n", frm->name);
        }
        else if (blk_arr[blk_pos] <= blk_arr[blk_pos - 1])
        {
            int len = blk_arr[blk_pos - 1] - blk_arr[blk_pos] + 1;
            char* prefix_blk = create_blk(len);
            fprintf(fp, "\t\tjson += \"%s,\\\"%s\\\":{\";\n", prefix_blk, frm->name);
        }
        blk_pos++;
    }

    for (; count < size - 1; count++)
    {
        fld = fld_frm->fld;
        char *val = lua_code_gen(fld->name, fld->type, fld->size, fld->offset, fld->method);
        fprintf(fp, "\t\t%s", val);
        fprintf(fp, "\t\tjson += \"\\\"%s\\\":\\\"\" + str_%s + \"\\\",\";\n", fld->name, fld->name);
        fld_frm = fld_frm->next;
    }
    FLD* last = fld_frm->fld;
    char *val = lua_code_gen(last->name, last->type, last->size, last->offset, last->method);
    fprintf(fp, "\t\t%s", val);
    fprintf(fp, "\t\tjson += \"%s\" + \":\\\"\" + str_%s + \"\\\"\";\n", last->name, last->name);
    code_gen_sub(fp, frm);
    return;
}

void code_gen_sub(FILE* fp, FRM* frm)
{
    FRM* tmp = frm->child;
    int pos = 0;
    int tmp_blk;
    while (pos < frm->child_size)
    {
        code_gen(fp, tmp);
        tmp++;
        pos++;
    }
}

char* create_blk(int len)
{
    char* prefix_blk = (char*) malloc(sizeof(char) * len);
    char* tmp = "}";
    int j = 0;
    strcpy(prefix_blk, "");
    for (;j < len; j++)
    {
        strcat(prefix_blk, tmp);
    }
    return prefix_blk;
}

char* lua_code_gen(char* name, char* type, int size, int offset, char* method)
{
    char* val;
    lua_getglobal(L, "code_gen");
    lua_pushstring(L, name);
    lua_pushstring(L, type);
    lua_pushnumber(L, size);
    lua_pushnumber(L, offset);
    lua_pushstring(L, method);
    lua_call(L, 5, 1);
    val = (char*) lua_tostring(L, -1);
    lua_pop(L,1);
    return val;
}


void byte_gen(FILE* fp, FRM* frm)
{
    printf("byte gen : %s\n", frm->name);
    FLD_FRM* fld_frm = frm->fld_frm;
    int size = frm->fld_size;
    int count = 0;
    FLD* fld = NULL;

    for (; count < size; count++)
    {
        fld = fld_frm->fld;
        char *val = lua_byte_gen(fld->name, fld->type, fld->size, fld->offset, fld->gen);
        //printf("val: %s\n", val);
        fprintf(fp, "\t\t%s", val);
        fprintf(fp, "\t\tbt = CodecUtil.merge(bt, bytes_%s);\n", fld->name);
        fld_frm = fld_frm->next;
    }

    byte_gen_sub(fp, frm);
    return;
}

void byte_gen_sub(FILE* fp, FRM* frm)
{
    FRM* tmp = frm->child;
    int pos = 0;
    int tmp_blk;
    while (pos < frm->child_size)
    {
        byte_gen(fp, tmp);
        tmp++;
        pos++;
    }
}

char* lua_byte_gen(char* name, char* type, int size, int offset, char* gen)
{
    char* val;
    lua_getglobal(L, "byte_gen");
    lua_pushstring(L, name);
    lua_pushstring(L, type);
    lua_pushnumber(L, size);
    lua_pushnumber(L, offset);
    lua_pushstring(L, gen);
    lua_call(L, 5, 1);
    val = (char*) lua_tostring(L, -1);
    lua_pop(L,1);
    return val;
}