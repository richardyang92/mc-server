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

FLD* init_fld(HEAD* head, char* type, int num, char* method, char* gen)
{
	FLD* fld = (FLD*) malloc(sizeof(FLD));
	fld->name = head->name;
	fld->size = head->size;
	fld->type = type;
	fld->offset = num;
	fld->method = method;
	fld->gen = gen;
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
    printf("frame name: %s\n", frm->name);
	FLD_FRM* fld_frm = frm->fld_frm;
	while (fld_frm != NULL)
	{
		printf("name: %s, type: %s, size: %d, offset: %d, method: %s, gen: %s\n", fld_frm->fld->name, fld_frm->fld->type, fld_frm->fld->size, fld_frm->fld->offset, fld_frm->fld->method, fld_frm->fld->gen);
		fld_frm = fld_frm->next;
	}
	printf("fld_size: %d\n", frm->fld_size);
	printf("child_size: %d\n", frm->child_size);
	show_child_frm(frm);
}

void show_child_frm(FRM* frm)
{
	FRM* tmp = frm->child;
	int pos = 0;
	while (pos < frm->child_size)
	{
		show_fld_frm(tmp);
		tmp++;
		pos++;
	}
}
