#pragma once
#ifndef FRAME_TYPE_H
#define FRAME_TYPE_H
#include "resolve_type.h"

typedef struct lanyan_open_bb {
	byte begin;
	byte cmd;
	byte len;
	byte* bbh;
	int bbh_len;
	byte frmId;
	byte cs;
	byte end;
} _lanyan_open_bb;

_lanyan_open_bb* resolve_lanyan_open_bb_fn(byte* msg);


char* create_lanyan_open_bb_json_fn(_lanyan_open_bb* ptr);

#endif
