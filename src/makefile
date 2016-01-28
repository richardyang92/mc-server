libresolve_util.so: 
	cc -I ${JAVA_HOME}/include/linux/ -I ${JAVA_HOME}/include/ -I ~/IdeaProjects/mc-server/src -fPIC -shared -o $@ resolve_util.c frame_type.c resolve_type.c
clean:
	sudo rm -rf libresolve_util.so /lib/libresolve_util.so
deploy:
	sudo mv libresolve_util.so /lib
