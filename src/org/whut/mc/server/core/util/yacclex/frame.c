#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "frame.h"

char* get_var(char* yytext)
{
	char* p = yytext;
	int i = 0;
	
	while (*p != '\0' && ((*p >= 'a' && *p <= 'z') || (*p >= 'A' && *p <= 'Z') 			|| (*p >= '0' && *p <= '9') || *p == '_'))
	{
		i++;
		p++;
	}
	p = yytext;
	char* rlt = (char*) malloc(sizeof(char) * i);
	int j = 0;
	char* tmp = rlt;

	while (j < i)
	{
		*tmp = *p;
		tmp++;
		p++;
		j++;
	}

	return rlt;
}

IDT get_INTDECL()
{
	return int_type;
}

IDT get_CHARDECL()
{
	return char_type;
}

IDT get_BYTEDECL()
{
	return byte_type;
}

char* get_type_name(IDT idt)
{
	switch (idt)
	{
		case int_type:
			return "int";
			break;
		case char_type:
			return "char";
			break;
		case byte_type:
		    return "byte";
		    break;
		default:
			break;
	}
}

HEAD* init_head(char* name, int size)
{
	HEAD* head = (HEAD*) malloc(sizeof(HEAD));
	head->name = name;
	head->size = size;
	return head;
}

FLD* init_fld(HEAD* head, char* type, int num)
{
	FLD* fld = (FLD*) malloc(sizeof(FLD));
	fld->name = head->name;
	fld->size = head->size;
	fld->type = type;
	fld->offset = num;
	return fld;
}

FRM* init_frm_by_fld(FLD* fld)
{
	FRM* frm = (FRM*) malloc(sizeof(FRM));
	frm->fld_frm = (FLD_FRM*) malloc(sizeof(FLD_FRM));
	frm->fld_frm->fld = fld;
	frm->fld_frm->next = NULL;
	frm->fld_size = 1;
	frm->child = (FRM*) malloc(sizeof(FRM));
	frm->child_size = 0;
	return frm;
}

void add_fld2frm(FRM* frm, FLD* fld)
{
	FLD_FRM* fld_frm = frm->fld_frm;
	FLD_FRM* pre_fld_frm;
	while (fld_frm != NULL)
	{
		pre_fld_frm = fld_frm;
		fld_frm = fld_frm->next;
	}
	fld_frm = (FLD_FRM*) malloc(sizeof(FLD_FRM));
	pre_fld_frm->next = fld_frm;
	fld_frm->fld = fld;
	fld_frm->next = NULL;
	frm->fld_size++;
}

void add_child2frm(FRM* frm, FRM* sub)
{
	if (frm->child_size == 0)
	{
		frm->child = sub;
	} 
	else
	{
		frm->child = (FRM*) realloc(frm->child, (frm->child_size + 1) * sizeof(FRM));
		frm->child[frm->child_size] = *sub;
	}
	frm->child_size++;
}	

void show_fld_frm(FRM* frm)
{
	FLD_FRM* fld_frm = frm->fld_frm;
	while (fld_frm != NULL)
	{
		printf("name: %s, type: %s, size: %d, offset: %d\n", fld_frm->fld->name, fld_frm->fld->type, fld_frm->fld->size, fld_frm->fld->offset);
		fld_frm = fld_frm->next;
	}
	printf("fld_size: %d\n", frm->fld_size);	
}

void create_frame_type_h(FRM* frm, char* frame_name)
{
	FILE* fp;
	fp = fopen("frame_type.h", "w");
	fprintf(fp, "#pragma once\n");
	fprintf(fp, "#ifndef FRAME_TYPE_H\n");
	fprintf(fp, "#define FRAME_TYPE_H\n");
	fprintf(fp, "#include \"resolve_type.h\"\n");
	fprintf(fp, "\ntypedef struct %s {\n", frame_name);
	FLD_FRM* fld_frm = frm->fld_frm;
	while (fld_frm != NULL)
	{
		fprintf(fp, "\t");
		if (fld_frm->fld->size == 1)
		{
			fprintf(fp, "%s %s;\n", fld_frm->fld->type, fld_frm->fld->name);
		}
		else
		{
			fprintf(fp, "%s* %s;\n", fld_frm->fld->type, fld_frm->fld->name);
			fprintf(fp, "\tint %s_len;\n", fld_frm->fld->name);
		}
		fld_frm = fld_frm->next;
	}
	fprintf(fp, "} _%s;\n", frame_name);
	fprintf(fp, "\n_%s* resolve_%s_fn(byte* msg);\n", frame_name, frame_name);
	fprintf(fp, "\n\nchar* create_%s_json_fn(_%s* ptr);\n\n", frame_name, frame_name);
	fprintf(fp, "#endif\n");
	fclose(fp);
}

void create_frame_resolve_fn(FILE* fp, FRM* frm, char* frame_name)
{
    fprintf(fp, "\n\n_%s* resolve_%s_fn(byte* msg) {\n", frame_name, frame_name);

	FLD_FRM* fld_frm = frm->fld_frm;
	fprintf(fp, "\t_%s* ptr = (_%s*) malloc(sizeof(_%s));\n", frame_name, frame_name, frame_name);
	fprintf(fp, "\tbyte* bt = msg;\n");
	fprintf(fp, "\tbyte* sub = NULL;\n");
	while (fld_frm != NULL)
	{
		if (fld_frm->fld->size == 1)
		{
			if (strcmp(fld_frm->fld->type, "int") == 0)
			{
				fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(int));
				fprintf(fp, "\tptr->%s = resolve_int(sub);\n", fld_frm->fld->name);
			}
			else if (strcmp(fld_frm->fld->type, "char") == 0)
			{
				fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(char));
				fprintf(fp, "\tptr->%s = resolve_char(sub);\n", fld_frm->fld->name);
			}
			else if (strcmp(fld_frm->fld->type, "byte") == 0)
			{
			    fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(unsigned char));
                fprintf(fp, "\tptr->%s = resolve_byte(sub);\n", fld_frm->fld->name);
			}
		}
		else
		{
			if (strcmp(fld_frm->fld->type, "int") == 0)
			{
			    fprintf(fp, "\tptr->%s_len = %d;\n", fld_frm->fld->name, fld_frm->fld->size);
				fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(int) * fld_frm->fld->size);
				fprintf(fp, "\tptr->%s = resolve_int_arr(sub, %d);\n", fld_frm->fld->name, fld_frm->fld->size);
			}
			else if (strcmp(fld_frm->fld->type, "char") == 0)
			{
				fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(char) * fld_frm->fld->size);
				fprintf(fp, "\tptr->%s = resolve_char_arr(sub, %d);\n", fld_frm->fld->name, fld_frm->fld->size);
			}
			else if (strcmp(fld_frm->fld->type, "byte") == 0)
            {
                fprintf(fp, "\tptr->%s_len = %d;\n", fld_frm->fld->name, fld_frm->fld->size);
                fprintf(fp, "\tsub = sub_byte(bt, %d, %d);\n", fld_frm->fld->offset, (int) sizeof(unsigned char) * fld_frm->fld->size);
                fprintf(fp, "\tptr->%s = resolve_byte_arr(sub, %d);\n", fld_frm->fld->name, fld_frm->fld->size);
            }
		}
		fld_frm = fld_frm->next;
	}
	fprintf(fp, "\treturn ptr;\n");
	fprintf(fp, "}\n");
}

void create_frame_create_json_fn(FILE* fp, FRM* frm, char* frame_name)
{
    fprintf(fp, "\n\nchar* create_%s_json_fn(_%s* ptr) {\n", frame_name, frame_name);
    fprintf(fp, "\tchar* ch = (char*) malloc(sizeof(char) * strlen(\"%s\"));\n", frame_name);
    fprintf(fp, "\tch = append_prefix(ch, \"%s\");\n", frame_name);
    FLD_FRM* fld_frm = frm->fld_frm;
    while (fld_frm != NULL)
    	{
    		if (fld_frm->fld->size == 1)
    		{
    			if (strcmp(fld_frm->fld->type, "int") == 0)
    			{
    				fprintf(fp, "\tch = append_int(ch, \"%s\", ptr->%s);\n", fld_frm->fld->name, fld_frm->fld->name);
    			}
    			else if (strcmp(fld_frm->fld->type, "char") == 0)
    			{
    				fprintf(fp, "\tch = append_char(ch, \"%s\", ptr->%s);\n", fld_frm->fld->name, fld_frm->fld->name);
    			}
    			else if (strcmp(fld_frm->fld->type, "byte") == 0)
    			{
    			    fprintf(fp, "\tch = append_byte(ch, \"%s\", ptr->%s);\n", fld_frm->fld->name, fld_frm->fld->name);
    			}
    		}
    		else
    		{
    			if (strcmp(fld_frm->fld->type, "int") == 0)
    			{
    			    fprintf(fp, "\tch = append_int_arr(ch, \"%s\", ptr->%s, ptr->%s_len);\n", fld_frm->fld->name, fld_frm->fld->name, fld_frm->fld->name);
    			}
    			else if (strcmp(fld_frm->fld->type, "char") == 0)
    			{
    				fprintf(fp, "\tch = append_char_arr(ch, \"%s\", ptr->%s, ptr->%s_len);\n", fld_frm->fld->name, fld_frm->fld->name, fld_frm->fld->name);
    			}
    			else if (strcmp(fld_frm->fld->type, "byte") == 0)
                {
                    fprintf(fp, "\tch = append_byte_arr(ch, \"%s\", ptr->%s, ptr->%s_len);\n", fld_frm->fld->name, fld_frm->fld->name, fld_frm->fld->name);
                }
    		}
    		fld_frm = fld_frm->next;
    	}
    fprintf(fp, "\tch = append_suffix(ch);\n");
    fprintf(fp, "\treturn ch;\n");
    fprintf(fp, "}\n");

}

void create_frame_type_c(FRM* frm, char* frame_name)
{
	FILE* fp;
	fp = fopen("frame_type.c", "w");
	fprintf(fp, "#include \"frame_type.h\"\n");
	fprintf(fp, "#include \"resolve_type.h\"\n");
	fprintf(fp, "#include <stdlib.h>\n");
	fprintf(fp, "#include <string.h>\n");

    create_frame_resolve_fn(fp, frm, frame_name);
    create_frame_create_json_fn(fp, frm, frame_name);
	fclose(fp);
}

void show_child_frm(FRM* frm)
{
	FRM* tmp = frm->child;
	int pos = 0;
	while (pos < frm->child_size - 1)
	{
		show_fld_frm(tmp);
		tmp++;
		pos++;
	}
}
