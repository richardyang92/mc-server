%{
	#include "frame.h"
	#include "frame.tab.h"
%}

%%
"int"				{
						yylval.idt = get_INTDECL(); 
						return INT_DECL;
					}

"char"				{
						yylval.idt = get_CHARDECL(); 
						return CHR_DECL;
					}
"byte"				{
                        yylval.idt = get_BYTEDECL();
                        return BYTE_DECL;
                    }

":"					{return SPL;}
"{"					{return BBP;}
"}"					{return EBP;}
"["					{return BMP;}
"]"					{return EMP;}
";"					{return EOL;}
[0-9]+ 				{yylval.iVal = atoi(yytext); return NUM;}
[a-z]				{yylval.iChr = yytext[0]; return CHR;}
[a-zA-Z][A-Za-z_0-9]+ 	{
						yylval.str = get_var(yytext);
						return VAR;
					}
[ \t\n]				{}
%%
