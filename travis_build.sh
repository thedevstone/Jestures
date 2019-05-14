#!/bin/bash
set -e
gradle publish || gradle printVersion
cp -R Jestures/build/reports/ report
cp -R Jestures_Recorder/build/reports/ report