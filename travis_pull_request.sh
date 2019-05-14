#!/bin/bash
set -e
gradle check || gradle printVersion
cp -R Jestures/build/reports/ report
cp -R Jestures_Recorder/build/reports/ report