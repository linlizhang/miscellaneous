#!/bin/bash
INSTALL_DIR_NAME="build-master-1.0.0-SNAPSHOT"

if [[ $0 =~ "nojvm" ]]; then
    if [ -z "${JAVA_HOME}" ]; then
        echo "JAVA_HOME variable not set."
        echo "Update your environment with JAVA_HOME pointing to JDK 1.6 and restart the installer."
        exit 1
    fi

    jvm_version=$("${JAVA_HOME}/bin/java" -version 2>&1 | head -n1)
    if [[ ! "${jvm_version}" =~ "1.6." ]]; then
        echo "This installer requires JAVA_HOME to point to Java 1.6 but it uses ${jvm_version}."
        echo "Update your environment with JAVA_HOME pointing to JDK 1.6 and restart the installer."
        exit 2
    fi

elif [ "$(uname -m)" != "x86_64" ]; then
    echo "This installer was built for Linux 64-bit but your system's architecture is $(uname -m)."
    echo "Aborting."
    exit 3
fi
# Shebang line and INSTALL_DIR_NAME definition generated above

printUsageAndExit() {
    echo "Usage: bash $0 -d target_dir [-e env_type] [-D config_token1=config_value1 ...]"
    exit 1
}

if [ $# -eq 0 ]; then
    printUsageAndExit
fi

target_dir=
config_tokens=
env_type=

while getopts "d:D:e:" arg; do
    case "$arg" in
        d) target_dir="$OPTARG" ;;
        D) config_tokens="${config_tokens}-D $OPTARG " ;;
        e) env_type="$OPTARG" ;;
        *) printUsageAndExit
    esac
done

# target_dir is mandatory. If it is empty this means that the path was passed directly without option,
# eg xyz.cdi /path/to/target/dir
if [ -z "${target_dir}" ]; then
    printUsageAndExit
fi

if [ -n "${INSTALL_DIR_NAME}" ]; then
    target_dir=${target_dir}/${INSTALL_DIR_NAME}
fi

if [ -d ${target_dir} ]; then
    echo "Target directory \"${target_dir}\" already exists."
    echo "Aborting."
    exit 2
fi

# dos2unix is not available by default on Ubuntu. Let's check it and abort if it cannot be found
which dos2unix 1>/dev/null 2>/dev/null
if [ $? -ne 0 ]; then
    echo "dos2unix cannot be found. Please install it and restart the installation."
    echo "Aborting."
    exit 3
fi

ARCHIVE_START_LINE_NR=$(awk '/^#__ARCHIVE_BELOW__/ {print NR + 1; exit 0; }' $0)

echo "Creating target directory ${target_dir}"
mkdir -p ${target_dir}

CASFW_HOME="$(cd "${target_dir}" && pwd -P)"

echo "Unpacking distribution into ${CASFW_HOME}"
tail -n+${ARCHIVE_START_LINE_NR} $0 | tar pxz -C ${CASFW_HOME}

# Just to make sure that scripts are linux-friendly
find ${CASFW_HOME} -type f \
\( -name "*.sh" -o -name "casfw.info" -o -name ".casfwrc" \) \
-exec dos2unix {} 1>/dev/null 2>/dev/null \;

source ${CASFW_HOME}/etc/casfw.info
echo
echo "${CASFW_INSTALL_NAME} - ${CASFW_INSTALL_VERSION} (r${CASFW_SCM_REVISION})"

# handle environment type
if [ -n "${env_type}" ]; then
    echo
    if [ -r "${CASFW_HOME}/etc/casfw.properties.${env_type}" ]; then
        echo "Copying ${CASFW_HOME}/etc/casfw.properties.${env_type} into ${CASFW_HOME}/etc/casfw.properties so it is used as default configuration tokens file"
        mv "${CASFW_HOME}/etc/casfw.properties" "${CASFW_HOME}/etc/casfw.properties.$(date '+%Y%m%d_%H%M%S')"
        cp "${CASFW_HOME}/etc/casfw.properties.${env_type}" "${CASFW_HOME}/etc/casfw.properties"
    else
        echo "-e ${env_type} option specified but tokens file not found: ${CASFW_HOME}/etc/casfw.properties.${env_type} !"
        echo "Aborting."
        exit 4
    fi
fi

pre_install_hook="${CASFW_HOME}/bin/.pre-install.sh"
if [ -r "${pre_install_hook}" ]; then
    echo
    echo "Executing pre-installation steps from ${pre_install_hook}"
    bash "${pre_install_hook}"

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Pre-installation steps failed with code ${last_exit_code}."
        echo "Aborting."
        exit ${last_exit_code}
    fi
fi

echo
echo "Updating configuration files"
chmod 744 ${CASFW_HOME}/bin/config.sh
${CASFW_HOME}/bin/config.sh ${config_tokens}

last_exit_code=$?
if [ ${last_exit_code} -ne 0 ]; then
    exit ${last_exit_code}
fi

post_install_hook="${CASFW_HOME}/bin/.post-install.sh"
if [ -r "${post_install_hook}" ]; then
    echo
    echo "Executing post-installation steps from ${post_install_hook}"
    bash "${post_install_hook}"

    last_exit_code=$?
    if [ ${last_exit_code} -ne 0 ]; then
        echo "Post-installation steps failed with code ${last_exit_code}."
        exit ${last_exit_code}
    fi
fi

# Create version-less symbolic link if not existing yet (e.g. batch -> batch-1.2.3)
#versionless_name="$(basename ${CASFW_HOME} | sed 's/-[0-9].*$//')"
#versionless_path="$(dirname ${CASFW_HOME})/${versionless_name}"
#if [[ "${CASFW_HOME}" != "${versionless_path}" && ! -e "${versionless_path}" && ! -L "${versionless_path}" ]]; then
#    echo "Creating symbolic link ${versionless_path} -> ${CASFW_HOME}"
#    ln -s "$(basename ${CASFW_HOME})" "${versionless_path}"
#fi

echo
echo "Installation Completed Successfully"

exit 0