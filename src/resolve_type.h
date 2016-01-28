#pragma once
#ifndef RESOLVE_TYPE_H
#define RESOLVE_TYPE_H

typedef unsigned char byte;

int resolve_int(byte* bt);
char resolve_char(byte* bt);
byte resolve_byte(byte* bt);

int* resolve_int_arr(byte* bt, int size);
char* resolve_char_arr(byte* bt, int size);
byte* resolve_byte_arr(byte* bt, int size);

byte* sub_byte(byte* bt, int pos, int size);

char* append_int(char* ch, char* name, int value);
char* append_char(char* ch, char* name, char value);
char* append_byte(char* ch, char* name, byte bt);

char* append_int_arr(char* ch, char* name, int* value, int size);
char* append_char_arr(char* ch, char* name, char* value, int size);
char* append_byte_arr(char* ch, char* name, byte* bt, int size);

char* append_prefix(char* ch, char* frm_name);
char* append_suffix(char* ch);

int add(int first, int second);

#endif