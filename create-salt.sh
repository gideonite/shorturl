#!/bin/bash

SALT=resources/salt

rm $SALT

for i in `seq 1 10`
do
    echo -n $RANDOM >> $SALT
done

