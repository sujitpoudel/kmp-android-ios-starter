#!/bin/bash

set -e

TRUE=1
FALSE=0

SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PROJECT_PATH="$(dirname "$SCRIPT_PATH")"

initializedPath=""
projectName="starter-project"
projectGroup="com.example.company"
configFile=""
buildGradleKtsFile=""
settingsGradleKtsFile=""

# Change project name
function setProjectName() {
    defaultName="kmp-android-ios-starter"
    read -p "Project name(Default: ${defaultName}): " name
    name=${name:-"${defaultName}"}
    projectName="${name}"
    echo "${name}"
}

# Ask for folder
function whereToInitialize() {
    defaultLocation="${HOME}/projects"
    read -p "Set parent directory (Default: ${defaultLocation}): " location
    location=${location:-"${defaultLocation}"}
    echo $(eval echo ${location})
}

function copyProjectToPath() {
    toPath="${1}"
    projectName="${2}"
    mkdir -p "${toPath}/${projectName}"
    cp -r "${PROJECT_PATH}/" "${toPath}/${projectName}/"
    initializedPath="${toPath}/${projectName}"
    configFile="${initializedPath}/buildSrc/src/main/kotlin/Config.kt"
    buildGradleKtsFile="${initializedPath}/build.gradle.kts"
    settingsGradleKtsFile="${initializedPath}/settings.gradle.kts"
}

function getGroup() {
    defaultGroup="com.example.company"
    read -p "Project group(Default: ${defaultName}): " group
    group=${group:-"${defaultGroup}"}
    projectGroup="${group}"
    echo "${group}"
}

function setNameAndGroupToConfigAndSettings() {
    sed -i '' "s/com.example.company/${projectGroup}/g" "${configFile}"
    sed -i '' "s/kmp-android-ios-starter/${projectName}/g" "${configFile}"
    sed -i '' "s/kmp-android-ios-starter/${projectName}/g" "${settingsGradleKtsFile}"
}

function setKotlinxSerializationLibrary() {
    validResponse="${FALSE}"
    defaultResponse="Y"
    while [ "${validResponse}" -eq "${FALSE}" ]; do
        validResponse="${TRUE}"
        read -p "Setup kotlinx-serialization library? (Default: ${defaultResponse}): " response
        response=${response:-"${defaultResponse}"}
        if [[ "${response}" == "N" ]] || [[ "${response}" == "n" ]]; then
            sed -i '' '/const val serialization/d' "${configFile}"
            sed -i '' '/serialization/d' "${buildGradleKtsFile}"
            echo "Kotlinx Serialization library is not setup"
        elif [[ "${response}" == "Y" ]] || [[ "${response}" == "y" ]]; then
            echo "Kotlinx Serialization library is setup"
        else
            echo "Response is not valid. Please try again"
            validResponse="${FALSE}"
        fi
    done
}

function setCoroutinesLibrary() {
    validResponse="${FALSE}"
    defaultResponse="Y"
    while [ "${validResponse}" -eq "${FALSE}" ]; do
        validResponse="${TRUE}"
        read -p "Setup kotlin coroutines library? (Default: ${defaultResponse}): " response
        response=${response:-"${defaultResponse}"}
        if [[ "${response}" == "N" ]] || [[ "${response}" == "n" ]]; then
            echo "kotlin coroutines library is not setup"
            sed -i '' '/const val coroutines/d' "${configFile}"
            sed -i '' '/coroutines/d' "${buildGradleKtsFile}"
        elif [[ "${response}" == "Y" ]] || [[ "${response}" == "y" ]]; then
            echo "kotlin coroutines library is setup"
        else
            echo "Response is not valid. Please try again"
            validResponse="${FALSE}"
        fi
    done
}

function setKtor() {
    validResponse="${FALSE}"
    defaultResponse="Y"
    while [ "${validResponse}" -eq "${FALSE}" ]; do
        validResponse="${TRUE}"
        read -p "Set ktor library for networking? (Default: ${defaultResponse}): " response
        response=${response:-"${defaultResponse}"}
        if [[ "${response}" == "N" ]] || [[ "${response}" == "n" ]]; then
            echo "Ktor library is not setup"
            sed -i '' '/const val ktor/d' "${configFile}"
            sed -i '' '/ktor/d' "${buildGradleKtsFile}"
        elif [[ "${response}" == "Y" ]] || [[ "${response}" == "y" ]]; then
            echo "Ktor library is setup"
        else
            echo "Response is not valid. Please try again"
            validResponse="${FALSE}"
        fi
    done
}

function awsForRelease() {
    validResponse="${FALSE}"
    defaultResponse="Y"
    while [ "${validResponse}" -eq "${FALSE}" ]; do
        validResponse="${TRUE}"
        read -p "Install release script to release aws? (Default: ${defaultResponse}): " response
        response=${response:-"${defaultResponse}"}
        if [[ "${response}" == "N" ]] || [[ "${response}" == "n" ]]; then
            echo "Release Script not installed"
            rm -rf "${initializedPath}/scripts/release.sh"
        elif [[ "${response}" == "Y" ]] || [[ "${response}" == "y" ]]; then
            echo "Release script for AWS is installed in \"${initializedPath}/scripts\" folder"
        else
            echo "Response is not valid. Please try again"
            validResponse="${FALSE}"
        fi
    done
}

function setLocalProperties() {
    rm -rf "${initializedPath}/local.properties"
    defaultPath="${HOME}/Library/Android/sdk"
    read -p "Android SDK path (Default: ${defaultPath}): " path
    path=${path:-"${defaultPath}"}
    pathToSave=$(eval echo ${path})
    echo "sdk.dir=$pathToSave" > "${initializedPath}/local.properties"
    echo "SDK path set to ${pathToSave} in ${initializedPath}/local.properties"
}

function setup() {
    hasValidPath=${FALSE}
    while [ "${hasValidPath}" -eq "${FALSE}" ];
    do
        name=$(setProjectName)
        echo "Environment variables are expanded."
        location=$(whereToInitialize)
        fullPath="${location}/${name}"
        if [ ! -d "$fullPath" ]; then
            hasValidPath="${TRUE}"
        else
            echo "The path \"${fullPath}\" already exists. Please try again"
            echo "=========================================================="
        fi
    done
    echo "Creating folder now: ${fullPath}"
    copyProjectToPath "${location}" "${name}"
    setNameAndGroupToConfigAndSettings
    setKotlinxSerializationLibrary
    setCoroutinesLibrary
    setKtor
    awsForRelease
    setLocalProperties
    echo "Finished!"
}

setup