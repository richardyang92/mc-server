#include "resolve_type.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int resolve_int(byte* bt)
{
    return bytes2int(bt);
}

char resolve_char(byte* bt)
{
    char* c = (char*) bt;
    return *c;
}

byte resolve_byte(byte* bt)
{
    return bt[0];
}


int* resolve_int_arr(byte* bt, int size)
{
    int* tmp = (int*) malloc(sizeof(int) * size);

    int pos = 0;
    int count = 0;
    while (count < size)
    {
         byte* tBt = sub_byte(bt, pos, 4);
         tmp[pos] = bytes2int(tBt);
         pos += 4;
         count++;
    }

    return tmp;
}

char* resolve_char_arr(byte* bt, int size)
{
    char* ch = (char*) malloc(sizeof(char) * size);
    int pos = 0;
    for (; pos < size; pos++)
    {
        char tmp = (char) bt[pos];
        ch[pos] = tmp;
    }

    return ch;
}

byte* resolve_byte_arr(byte* bt, int size)
{
    return bt;
}


int bytes2int(byte* bytes)
{
    int tmp = (bytes[0] && 0xFF) << 24;
    tmp |= (bytes[1] && 0xFF) << 16;
    tmp |= (bytes[2] && 0xFF) << 8;
    tmp |= bytes[3]  & 0xFF;
    return tmp;
 }

byte* sub_byte(byte* bt, int pos, int size)
{
    byte* tBt = (byte*) malloc(sizeof(byte) * size);
    int i = 0;
    for (; i < size; i++)
    {
        tBt[i] = bt[pos + i];
    }
    return tBt;
}

char* append_int(char* ch, char* name, int value)
{
    return ch;
}

char* append_char(char* ch, char* name, char value)
{
    return ch;
}

char* append_byte(char* ch, char* name, byte bt)
{
    int size = strlen(ch);
    ch = (char*) realloc(ch, sizeof(char) * (size + strlen(ch) + 9));
    strcat(ch, "\"");
    strcat(ch, name);
    strcat(ch, "\":\"");
    char str[6];
    sprintf(str, "%d", bt);
    strcat(ch, str);
    strcat(ch, "\";");
    return ch;
}

char* append_int_arr(char* ch, char* name, int* value, int size)
{
    return ch;
}

char* append_char_arr(char* ch, char* name, char* value, int size)
{
    return ch;
}

char* append_byte_arr(char* ch, char* name, byte* bt, int size)
{
     int size_ch = strlen(ch);
     ch = (char*) realloc(ch, sizeof(char) * (2 * size_ch + 9));
     strcat(ch, "\"");
     strcat(ch, name);
     strcat(ch, "_arr\":[{");
     int count = 0;
     for (; count < size; count++)
     {
        /*char str[6];
        sprintf(str, "%d", bt[count]);*/
        //strcat(ch, str);
        ch = append_byte(ch, name, bt[count]);
     }
     strcat(ch, "}];");
    return ch;
}

char* append_prefix(char* ch, char* frm_name)
{
    int size = strlen(frm_name);
    int pos = 0;

    for (; pos < size; pos++)
    {
        ch[pos] = frm_name[pos];
    }
    ch = (char*) realloc(ch, sizeof(char) * (size + 2));
    strcat(ch, ":{");
    return ch;
}

char* append_suffix(char* ch)
{
    int size = strlen(ch);
    ch[size - 1] = '}';
    return ch;
}

int add(int first, int second)
{
    printf("a + b = %d\n", first + second);
    return first + second;
}