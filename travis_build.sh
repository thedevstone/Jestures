#!/bin/bash
set -e
gradle check publish
mkdir -p report
cp --parent */build/reports build/reports report -R
