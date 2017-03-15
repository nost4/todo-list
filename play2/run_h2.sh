#!/bin/bash

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
cd $SCRIPT_DIR
java -cp h2-1.4.193.jar org.h2.tools.Shell -user sa -url jdbc:h2:./todo/db/todoapp
