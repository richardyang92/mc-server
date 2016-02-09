#pragma once
#ifndef RESOLVE_H
#define RESOLVE_H
#include "frame.h"

void codec(FRM* frm);
void blk_gen(FRM* frm, int blk);
void blk_gen_sub(FRM* frm, int blk);
void code_gen(FILE* fp, FRM* frm);
void code_gen_sub(FILE* fp, FRM* frm);
char* lua_code_gen(char* name, char* type, int size, int offset, char* method);
char* create_blk(int len);

void byte_gen(FILE* fp, FRM* frm);
void byte_gen_sub(FILE* fp, FRM* frm);
char* lua_byte_gen(char* name, char* type, int size, int offset, char* method);

#endif