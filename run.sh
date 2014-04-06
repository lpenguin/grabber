#!/bin/bash

jar=out/artifacts/grabber_jar/grabber.jar
main_class=grabber.App
java -cp $jar $main_class $@
