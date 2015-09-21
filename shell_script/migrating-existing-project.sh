#!/bin/bash

#-------------------------------------------------------------------------------------------------------
#
# Environment Variable 
# 
# CASFW_HOME                         Point Base directory of "build installer" installation.
#
# JOB_HOME                           Point at the directory storing hudson jobs.
#
# NO_CHANGE_TEMPLATE                 Be used to replace the "<properties/>" attribute
#
# USER_WITH_AUTHENTICATION_TEMPLATE  Be used to replace the "</hudson.security.AuthorizationMatrixProperty>" attribute
#
# PM_WITH_AUTHENTICATION_TEMPLATE    Be used to replace the "</hudson.security.AuthorizationMatrixProperty>" attribute
#
# NO_AUTHENTICATION_TEMPLATE         Be used to replace the "</properties>" attribute
#
#--------------------------------------------------------------------------------------------------------

printUsageAndExit(){
	echo "Usage: bash $0 migrating_jobs_file epr_id project_manager_e-mail [location of decompressed files]"
		migrating_jobs_file must be the tar file
		epr_id must be digital and length of 6
		project_manager_email must match email address format
	exit 1
}


# the three parameters are required
if [[ $# -lt 3 ]]; then
	printUsageAndExit
fi

# the first parameter is a file,whose extension is "tar"
file_regex=".tar$"
if [[ ! "$1" =~ $file_regex ]]; then
	printUsageAndExit
fi

# check the second parameter, epr_id must be digital and length of 6
epr_regex='^[0-9][0-9]{4}[0-9]$'
if [[ ! "$2" =~ $epr_regex ]]; then 
	printUsageAndExit
fi

# check the third parameter,the parameter must match email address format
mail_regex='[a-z0-9]*@hp.com'
if [[ ! "$3" =~ $mail_regex ]]; then
	printUsageAndExit
fi

#location of the script file
CASFW_HOME="$(cd "$(dirname "$0")/.." && pwd -P)"

#hudson job directory
if [ -d "$4" ]; then
	JOB_HOME="$(cd $(ls -d $4 | tail -n1) && pwd -P)"
else
	JOB_HOME="${CASFW_HOME}/hudson-jobs" 
fi

# template string for modifying job configuration file
NO_CHANGE_TEMPLATE="s/<properties\/>/<properties>\n \ <hudson.security.AuthorizationMatrixProperty>\n \
  <permission>hudson.model.Run.Update:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Build:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Read:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Workspace:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Run.Update:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Run.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Configure:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Build:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Read:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Workspace:@@email_addr@@<\/permission>\n \
 <\/hudson.security.AuthorizationMatrixProperty>\n \
<\/properties>/g"

USER_WITH_AUTHENTICATION_TEMPLATE="s/<\/hudson.security.AuthorizationMatrixProperty>/<permission>hudson.model.Run.Update:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Build:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Read:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Workspace:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
 <\/hudson.security.AuthorizationMatrixProperty>/g"
 

PM_WITH_AUTHENTICATION_TEMPLATE="s/<\/hudson.security.AuthorizationMatrixProperty>/<permission>hudson.model.Run.Update:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Run.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Configure:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Build:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Read:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Workspace:@@email_addr@@<\/permission>\n \
 <\/hudson.security.AuthorizationMatrixProperty>/g"

NO_AUTHENTICATION_TEMPLATE="s/<\/properties>/<hudson.security.AuthorizationMatrixProperty>\n \
  <permission>hudson.model.Run.Update:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Build:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Read:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Item.Workspace:ROLE_USERS-@@epr_id@@-DEV<\/permission>\n \
  <permission>hudson.model.Run.Update:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Run.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Delete:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Configure:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Build:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Read:@@email_addr@@<\/permission>\n \
  <permission>hudson.model.Item.Workspace:@@email_addr@@<\/permission>\n \
 <\/hudson.security.AuthorizationMatrixProperty>\n \
<\/properties>/g"

echo "migrating start"

#when error occur,clean up temporary directory
cleanup_script()
{
	echo "clean up temporary directory"
	rm -fr ${CASFW_HOME}/hudson-output 1>/dev/null 2>/dev/null
	if [ $? -ne 0 ]; then
		echo "Cannot delete the directory ${CASFW_HOME}/hudson-output, Please check user's privilege"
		echo "Aborting"
		exit 1
	fi
	exit 1
}

# create temporary directory
mkdir -p ${CASFW_HOME}/hudson-output >/dev/null
if [ $? -ne 0 ]; then
	echo "Cannot create directory:${CASFW_HOME}/hudson-output,Please check user's privilege"
	echo "Aborting"
	exit 1
fi

# decompress tar files

echo "decompress tar files.."

tar -xf $1 -C "${CASFW_HOME}/hudson-output/." 
if [ $? -ne 0 ]; then
        echo "Cannot decompress the files.Please check user's privilege"
        echo "Aborting"
        cleanup_script
fi

if [ ! -d "${JOB_HOME}" ]; then
	mkdir -p "${JOB_HOME}"
fi

# if the existing jobs' configuration files do not contain project-based security, then replace the attribute "<properties/>" with NO_CHANGE_TEMPLATE 
 
# if the existing jobs' configuration files do not contain project-based security but the attribute "<properties/>" has changed, 
#   then replace the attribute "</properties>" with NO_AUTHENTICATION_TEMPLATE 
echo "modify job's config.xml file.."

pushd "${CASFW_HOME}/hudson-output"
for config_dir in $(ls -d *);do
	node_attri_nochange=$(grep "<properties/>" "${config_dir}"/config.xml)
	node_attri_permission=$(grep "</hudson.security.AuthorizationMatrixProperty>" "${config_dir}"/config.xml)
	node_attri_nopermission=$(grep "</properties>" "${config_dir}"/config.xml)
	if [ -n "${node_attri_nochange}" ]; then
		sed -i -e "${NO_CHANGE_TEMPLATE}" "${config_dir}"/config.xml
		sed -i -e "s/@@epr_id@@/$2/g" "${config_dir}"/config.xml
		sed -i  -e "s/@@email_addr@@/$3/g" "${config_dir}"/config.xml
	elif [ -n "${node_attri_permission}" ]; then
		eprIdValue=$(grep "ROLE_USERS-$2-DEV" "${config_dir}"/config.xml)
		if [ -z "${eprIdValue}" ]; then
			sed -i -e "${USER_WITH_AUTHENTICATION_TEMPLATE}" "${config_dir}"/config.xml
			sed -i -e "s/@@epr_id@@/$2/g" "${config_dir}"/config.xml
		fi
		pmValue=$(grep "[a-z\.]*$3</permission>$" "${config_dir}"/config.xml)
		if [ -z "${pmValue}" ]; then
		    sed -i -e "${PM_WITH_AUTHENTICATION_TEMPLATE}" "${config_dir}"/config.xml	
			sed -i -e "s/@@email_addr@@/$3/g" "${config_dir}"/config.xml
		fi
	elif [[ -z "${node_attri_permission}" && -n "${node_attri_nopermission}" ]]; then
		sed -i -e "${NO_AUTHENTICATION_TEMPLATE}" "${config_dir}"/config.xml
		sed -i -e "s/@@epr_id@@/$2/g" "${config_dir}"/config.xml
		sed -i -e "s/@@email_addr@@/$3/g" "${config_dir}"/config.xml
	else
		echo " ${config_dir}/config.xml format error,please add privilege manully"
		continue
	fi
	jobName=$(find "${config_dir}" -name "$2_*" -type d )
	if [ -n "${jobName}" ]; then
		cp -R "${config_dir}" "${JOB_HOME}" 1>/dev/null 2>/dev/null
	else
		mv "${config_dir}" "$2"_"${config_dir}"
		cp -R "$2"_"${config_dir}" "${JOB_HOME}" 1>/dev/null 2>/dev/null
	fi
done
popd


#delete temporary directory
rm -fr ${CASFW_HOME}/hudson-output >/dev/null

echo "migrating successfully"
