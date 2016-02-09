lua_path := /usr/include/lua5.1/

frame: frame.l frame.y
	bison -dv frame.y
	flex frame.l
	gcc -I ${lua_path} -o $@ frame.tab.c lex.yy.c frame.c resolve.c -l lua5.1 -ll -g
clean:
	rm -rf frame frame.tab.c frame.tab.h lex.yy.c frame.output frame_type.h frame_type.c
