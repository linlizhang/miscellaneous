#!/bin/bash
# scratch source code in inside the Java Virtual Machine

BASE_URL="http://web.informatik.uni-bonn.de/IV/martini/Lehre/Veranstaltungen/SS00/InformatikII/JavaSimulation/"

curl -O "http://web.informatik.uni-bonn.de/IV/martini/Lehre/Veranstaltungen/SS00/InformatikII/JavaSimulation/sourcecode.html"

FILE_NAME="sourcecode.html"
JAVA_TMP="java_temp_link"
JAVA_LINK="java_link"

grep "^<a href" $FILE_NAME > $JAVA_TMP
grep -Po '".*?"' $JAVA_TMP > $JAVA_LINK



while read line;
do
    line=$(echo $line | sed 's/"\([^"]*\)"/\1/')
    url=$BASE_URL$line
    echo $url
    curl -O "$url" & 
done < ${JAVA_LINK}

#delete temporary files
rm -f $JAVA_TMP
rm -f $JAVA_LINK
rm -f $FILE_NAME
