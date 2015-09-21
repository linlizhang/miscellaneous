#!/bin/bash
CASFW_HOME="$(cd "$(dirname "$0")/" && pwd -P)" 

CAS_VAR_DIR_NAME="/var/crash/hudson-var"
# if CAS_VAR_DIR_NAME directory exists, then delete it first,then create it.
# if CAS_VAR_DIR_NAME directory does not exist,then create it
if [[ -e "{CAS_VAR_DIR_NAME}" && -d "{CAS_VAR_DIR_NAME}"]]; then
	rm -rf ${CAS_VAR_DIR_NAME} 1>/dev/null 2>/dev/null
	if [ $? -ne 0]; then
		echo "Cannot delete the ${CAS_VAR_DIR_NAME}. Please user's privileges"
		echo "Aborting"
		exit 31
	fi
	mkdir -p ${CAS_VAR_DIR_NAME}
	if [ $? -ne 0]; then
		echo "Cannot create the ${CAS_VAR_DIR_NAME}. Please user's privileges"
		echo "Aborting"
		exit 31
	fi
else
	mkdir -p ${CAS_VAR_DIR_NAME}
	if [ $? -ne 0]; then
		echo "Cannot create the ${CAS_VAR_DIR_NAME}. Please user's privileges"
		echo "Aborting"
		exit 31
	fi
fi
# delete .m2 directory
if [ -e "${HOME}/.m2"]; then
	rm -rf ${HOME}/.m2 1>/dev/null 2>/dev/null
	if [ $? -ne 0 ]; then
		echo "Cannot delete the ${HOME}/.m2. Please user's privileges"
		echo "Aborting"
		exit 31
	fi
fi
#delete installer directory
cd build*
if [$? -ne 0]; then
	rm -rf build*
	if [ $? -ne 0 ]; then
		echo "Cannot delete the build* directory. Please user's privileges"
		echo "Aborting"
		exit 31
	fi
fi