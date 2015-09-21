#!/bin/bash

BASE_HOME="$(cd "$(dirname '$0')" && pwd -P)"

EXTRACTING_JOB_LOG="${BASE_HOME}"/extracting_job.log

# parameter is required
if [ -z "$1" ]; then
	echo "Usage : bash extract-config.sh para
		para : the full path of jobs"
	echo "Aborting"
	echo 1
fi

if [ ! -d "$1" ]; then
	echo "parameter must be directory"
	echo "Aborting"
	echo 2
fi

#create a temporary directory
mkdir -p "${BASE_HOME}"/temporary >/dev/null 

#when error occure,clean the temporary directory
cleanup_script()
{
	echo "clean up temporary directory"
	rm -fr ${BASE_HOME}/temporary 1>/dev/null 2>/dev/null
	exit 1
}

#create directory and copy config.xml from the given path
for fileName in $( ls "$1"); do
	
	if [ -d "${1}/${fileName}" ]; then
		result=$(find "$1/$fileName" -maxdepth 2 -type f -name "config.xml")
		if [ -n "${result}" ]; then
			mkdir -p "${BASE_HOME}"/temporary/${fileName} >/dev/null
		
			if [ $? -ne 0 ]; then
				echo "Create directory error, please check user's privilege"
				echo "Aborting"
				cleanup_script
			fi

			cp "$1"/${fileName}/config.xml "${BASE_HOME}"/temporary/${fileName} >/dev/null		
			if [ $? -ne 0 ]; then
				echo "Copy file error"
				echo "Aborting"
				cleanup_script
			fi
		fi		
	fi
done
# modified 30/01/2012
#CUR_HOME=$PWD

#tar files in temporary directory
pushd "${BASE_HOME}/temporary/" 

#create log file

touch "${EXTRACTING_JOB_LOG}"

tar -zcvf "${BASE_HOME}"/hudson-jobs.tar * >>"${EXTRACTING_JOB_LOG}" 2>&1  
if [ $? -ne 0 ]; then
	echo "Decompress files failure,please check the reason"
	echo "Aborting"
	cleanup_script
fi
popd 
#delete temporary directory
rm -fr ${BASE_HOME}/temporary 1>/dev/null 2>/dev/null

