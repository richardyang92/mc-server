#!/bin/sh
echo "run test"
A="$1"
B="clean"
if [ "$A" = "$B" ]
then rm -rf frame_type.c frame_type.h
else 
./frame < "$A"
fi
