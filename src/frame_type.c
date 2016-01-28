#include "frame_type.h"
#include "resolve_type.h"
#include <stdlib.h>
#include <string.h>


_lanyan_open_bb* resolve_lanyan_open_bb_fn(byte* msg) {
	_lanyan_open_bb* ptr = (_lanyan_open_bb*) malloc(sizeof(_lanyan_open_bb));
	byte* bt = msg;
	byte* sub = NULL;
	sub = sub_byte(bt, 0, 1);
	ptr->begin = resolve_byte(sub);
	sub = sub_byte(bt, 1, 1);
	ptr->cmd = resolve_byte(sub);
	sub = sub_byte(bt, 2, 1);
	ptr->len = resolve_byte(sub);
	ptr->bbh_len = 13;
	sub = sub_byte(bt, 3, 13);
	ptr->bbh = resolve_byte_arr(sub, 13);
	sub = sub_byte(bt, 16, 1);
	ptr->frmId = resolve_byte(sub);
	sub = sub_byte(bt, 17, 1);
	ptr->cs = resolve_byte(sub);
	sub = sub_byte(bt, 18, 1);
	ptr->end = resolve_byte(sub);
	return ptr;
}


char* create_lanyan_open_bb_json_fn(_lanyan_open_bb* ptr) {
	char* ch = (char*) malloc(sizeof(char) * strlen("lanyan_open_bb"));
	ch = append_prefix(ch, "lanyan_open_bb");
	ch = append_byte(ch, "begin", ptr->begin);
	ch = append_byte(ch, "cmd", ptr->cmd);
	ch = append_byte(ch, "len", ptr->len);
	ch = append_byte_arr(ch, "bbh", ptr->bbh, ptr->bbh_len);
	ch = append_byte(ch, "frmId", ptr->frmId);
	ch = append_byte(ch, "cs", ptr->cs);
	ch = append_byte(ch, "end", ptr->end);
	ch = append_suffix(ch);
	return ch;
}
