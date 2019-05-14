#!/bin/bash
set -e
./gradlew check projectReport artifactoryPublish
mkdir -p report
cp --parent */build/reports build/reports report -R