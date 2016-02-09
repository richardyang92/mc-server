%{
	#include <stdio.h>
	#include "frame.h"
	#include "resolve.h"

	FRM* frm_global;
	char* frame_name;
%}

%union {
	int iVal;
	char iChr;
	char* str;
	IDT idt;
	HEAD* head; 
	FLD* fld;
	FRM* frm;
};

%token <iVal>NUM
%token <iChr>CHR
%token <str>VAR 
%token <idt>INT_DECL <idt>CHR_DECL <idt>BYTE_DECL
%token SPL BBP EBP BMP EMP EOL
%type <idt>type
%type <str>name
%type <str>method
%type <head>head
%type <fld>field
%type <frm>frame
%type <frm>fields
%%
frame:
	head BBP fields EBP
	{
		$$ = $3;
		$$->name = $1->name;
		frm_global = $$;
		/*printf("frame start:\n");
		show_fld_frm($$);
		printf("frame end.\n");*/
	}
	;
fields:
	field
	{
		$$ = init_frm_by_fld($1);
		/*printf("fields 1 start:\n");
		show_fld_frm($$);
		printf("fields 1 end.\n");*/
	} 
	| fields field
	{
		add_fld2frm($1, $2);
		$$ = $1;
		/*printf("fields 2 start:\n");
		show_fld_frm($$);
		printf("fields 2 end.\n");*/
	}
	| fields frame
	{
		add_child2frm($1, $2);
		$$ = $1;
		/*printf("fields 3 start:\n");
		show_child_frm($$);
		printf("fields 3 end.\n");*/
	}
	;
field:
	head type SPL NUM SPL method SPL method EOL
	{
		$$ = init_fld($1, get_type_name($2), $4, $6, $8);
	}
	;
head:
	name SPL
	{
		if (frame_name == NULL)
		{
			frame_name = $1;
			//printf("frame_name: %s\n", frame_name);
		}
		$$ = init_head($1, 1);
	}
	| name BMP NUM EMP SPL
	{
		$$ = init_head($1, $3);
	}
	;
type:
	INT_DECL 
	{
		$$ = $1; 
	}
	| CHR_DECL 
	{
		$$ = $1;
	}
	| BYTE_DECL
    	{
    		$$ = $1;
    	}
	;
	name:
	VAR 
	{
		$$ = $1; 
	}
	;
	method:
	BMP VAR EMP
	{
		$$ = $2;
	}
%%

main(int argc, char **argv)
{
	yyparse();
	show_fld_frm(frm_global);
    codec(frm_global);
}

yyerror(char *s)
{
	fprintf(stderr, "error: %s\n", s);
}
