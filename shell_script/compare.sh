#!/bin/sh
BASEDIR=${PWD}
oIFS="$IFS"
IFS=$'\r\n'
#find . ! -path "./doc/*" ! -path "./**/test/*" -name "*.java" ! -name "*Test.java" ! -name "Test*.java"> names.lst
while read name
do
    bn="$( basename "$name" )"
    num="$( egrep "/${bn}" names.lst | wc -l )"
    if test ${num} -gt 1 
    then
        names="$(grep "/${bn}" names.lst | sort)"
        strArr=($names)
        for (( i=0; i<=$(( $num - 1 )); i++))
        do
            name1="$(echo "${strArr[$i]}" | egrep -o "main/.*")"
            #echo "*****$name1" 
            let j=i+1;
            for ((; j<=$(( $num - 1 )); j++))
            do
                name2="$(echo "${strArr[$j]}" | egrep -o "main/.*")"
                #echo "====$name2"
                if [ "$name1" = "$name2" ]
                then
                    echo "${strArr[$i]}" >>out.lst
                    echo "${strArr[$j]}" >>out.lst
                    #sed -i "/$name/d" names.lst
                    echo "*********************************"  >>out.lst
                fi
            done 
        done
    fi
done < names.lst
