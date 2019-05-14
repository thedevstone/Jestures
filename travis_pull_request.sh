#!/bin/bash
set -e
gradle build || gradle printVersion
cp -R */build/reports/* report