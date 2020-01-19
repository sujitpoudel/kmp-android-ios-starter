#!/bin/bash

set -e

TRUE=1
FALSE=0

RED=$'\e[1;31m'
GREEN=$'\e[1;32m'
RESET_COLOR=$'\033[0m'

SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PROJECT_PATH="$(dirname "$SCRIPT_PATH")"

LOCAL_MAVEN_PATH="${HOME}/.m2"
ORG_PATH="com/github/urbancompass"

CONFIG_FILE="${PROJECT_PATH}/buildSrc/src/main/kotlin/Config.kt"

GRADLEW="${PROJECT_PATH}/gradlew"
DATE=$(date '+%Y.%m.%d.%H.%M.%S')

targets=("kmp-compass-api-ios64" "kmp-compass-api-iossim" "kmpcompass-android" "kmpcompass-common")

function readProjectName() {
  nameLineRaw=$(grep "const val libraryName" "${CONFIG_FILE}")
  nameLine="$(echo -e "${nameLineRaw}" | tr -d '[:space:]')"
  echo "${nameLine}" | cut -d'"' -f 2
}

PROJECT_NAME=$(readProjectName)
AWS_BUCKET="<AWS BUCKET HERE>" # Change this
AWS_RELEASE_FOLDER="${AWS_BUCKET}/${PROJECT_NAME}"


function logError() {
  printf "%s%s%s\n" "${RED}" "${1}" "${RESET_COLOR}"
}

function logInfo() {
  printf "%s%s%s\n" "${GREEN}" "${1}" "${RESET_COLOR}"
}

function getVersionCode() {
  versionLineRaw=$(grep "const val thisLibrary" "${CONFIG_FILE}")
  versionLine="$(echo -e "${versionLineRaw}" | tr -d '[:space:]')"
  echo "${versionLine}" | cut -d'"' -f 2
}

version=$(getVersionCode)

function isProjectGitDirty() {
  echo ${FALSE}
  return 1
  if [[ $(git diff --shortstat 2> /dev/null | tail -n1) != "" ]]; then
    echo ${TRUE}
  else
    echo ${FALSE}
  fi
}

function istMasterBranch() {
  branch=$(git rev-parse --abbrev-ref HEAD)
  if [[ "${branch}" == "master" ]]; then
    echo ${TRUE}
  else
    echo ${FALSE}
  fi
}

function isAlreadyReleasedInGit() {
  git fetch --tags --force
  if [ "$(git tag -l "${version}")" ]; then
      echo ${TRUE}
  else
      echo ${FALSE}
  fi
}

function isAlreadyReleasedInAws() {
  folder_out="$( aws s3 ls "${AWS_RELEASE_FOLDER}/${version}/")"
  if [ -n "${folder_out}" ]; then
    echo "${TRUE}"
  else
    echo "${FALSE}"
  fi
}

function backupLocalRepo() {
  backupFolderName="repository.${DATE}"
  if [[ -d "${LOCAL_MAVEN_PATH}/repository" ]]; then
    mv "${LOCAL_MAVEN_PATH}/repository" "${LOCAL_MAVEN_PATH}/${backupFolderName}"
  fi
}

function restoreLocalRepo() {
  backupFolderName="repository.${DATE}"
  releaseDate=$(date '+%Y.%m.%d.%H.%M.%S')
  releaseBackupFolder="${PROJECT_NAME}.release.${releaseDate}"
  if [[ -d "${LOCAL_MAVEN_PATH}/repository" ]]; then
    mv "${LOCAL_MAVEN_PATH}/repository" "${LOCAL_MAVEN_PATH}/${releaseBackupFolder}"
  fi
  if [[ -d "${LOCAL_MAVEN_PATH}/${backupFolderName}" ]]; then
    mv "${LOCAL_MAVEN_PATH}/${backupFolderName}" "${LOCAL_MAVEN_PATH}/repository"
  fi
}

function build() {
  ${GRADLEW} clean
  ${GRADLEW} :publishToMavenLocal
}

function releaseToGithub() {
  logInfo "Creating git tag ${version}"
  set -x
  git tag "${version}"
  git push origin "${version}"
  set +x
  logInfo "Pushed the tag to origin"
}

function releaseToAwsS3() {
  logInfo "Releasing artifacts to aws"
  for target in "${targets[@]}"; do
    relative_path="${ORG_PATH}/${target}"
    set -x
    aws s3 cp "${LOCAL_MAVEN_PATH}/repository/${relative_path}/${version}" "${AWS_RELEASE_FOLDER}/${relative_path}/${version}/" --recursive
    set +x
  done
}

function release() {
  logInfo "Releasing version ${version}"
  if [[ $(isAlreadyReleasedInGit) -ne ${TRUE} && $(isAlreadyReleasedInAws) -ne ${TRUE} ]]; then
    if [[ $(isProjectGitDirty) -eq ${TRUE} ]]; then
      logError "Current project has uncommitted changes. Please commit, or stash them"
      exit 1
    fi
    if [[ $(istMasterBranch) -eq ${FALSE} ]]; then
      logError "Can only cut the release from master branch."
      exit 1
    fi
    backupLocalRepo
    build
    releaseToGithub
    releaseToAwsS3
    restoreLocalRepo
  else
    logError "The version you are trying to release is already released either on git tag, or aws: ${version}"
    exit 1
  fi
  logInfo "Finished!"
}

# release
echo "${PROJECT_NAME}"
