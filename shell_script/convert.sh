#!/bin/bash
BASE_HOME="$(cd "$(dirname '$0')" && pwd -P)"

#Manipulate file with template file

manipulateFile() {
    if [ -f "$1" ]; then
        sed -i "s|$|  |g" "$1"
        #Hardcode the insert command
        sed -i '/备注|/a |-------------|----------|----------|--------------|' "$1"
        sed -i '/参数说明|/a |-------------|----------|----------|--------------|' "$1"
        while read -r line
        do
            val1=$(echo $line | awk -F\= '{print $1}')
            val2=$(echo $line | awk -F\= '{print $2}')
            echo $val1 $val2
            sed -i "s|$val1|$val2|g" "$1"
        done < "$2"
        fileName="$(basename $1)"
        filePrefix="$(echo $fileName | awk -F\. '{print $1}')"
        cp $1 $filePrefix.md
    fi
}

#parameter is required
if [ "$#" -ne 2 ] && ! [ -f "$2" ]; then
    echo "Usage: bash convert.sh para template_file 
            para: the full path of files to convert"
    echo "Aborting"
    exit 1
fi

if [ -d "$1" ]; then
    for file in $1/*.txt
    do
        manipulateFile $file $2
    done 
else
    manipulateFile "$1" $2
fi

