frame: frame.l frame.y
	bison -dv frame.y
	flex frame.l
	gcc -o $@ frame.tab.c lex.yy.c frame.c -ll -g
clean:
	rm -rf frame frame.tab.c frame.tab.h lex.yy.c frame.output frame_type.h frame_type.c

test:
	cc -o test test.c frame_type.c resolve_type.c -g
test_clean:
	rm -rf test
