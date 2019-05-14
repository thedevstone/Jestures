#!/bin/bash
set -e
./gradlew check publish
mkdir -p report
cp --parent */build/reports build/reports report -R
