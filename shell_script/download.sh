#!/bin/bash
for line in $(cat url.txt)
do
   wget --content-dispostion "$line"  &
done 
