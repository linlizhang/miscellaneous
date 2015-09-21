#!/bin/bash
for file in $(ls *.bar)
do
    fileName=`basename $file .bar` 
    mkdir -p $fileName
    unzip -ajx $file "*/*/*/*/pom.xml" -d $fileName 2>&1>/dev/null
    mv $fileName/pom.xml $fileName/pom.pom
    mvn deploy:deploy-file -Dfile=$file \
       -DrepositoryId=releases \
       -Durl=http://cas-repo.austin.hp.com/nexus/content/repositories/releases/ \
       -DpomFile=$fileName/pom.pom -X

done;


