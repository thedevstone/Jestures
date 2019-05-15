#!/bin/bash
set -e
./gradlew check publish
mkdir -p report
cp --parent */build/reports build/reports report -R
java -jar ./codacy-coverage-reporter-6.0.0-assembly.jar report -l Java -r Jestures*/build/reports/jacoco/test/jacocoTestReport.xml

