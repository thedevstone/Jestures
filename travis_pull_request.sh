#!/bin/bash
set -e
./gradlew check artifactoryPublish
mkdir -p report
cp --parent */build/reports build/reports report -R