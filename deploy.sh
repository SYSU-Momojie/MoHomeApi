#!/bin/bash

mvn install -Dmaven.test.skip=true
ps aux|grep mo-app|awk '{print $2}'|xargs kill -9
nohup java -jar ./app/target/mo-app-1.0.0-SNAPSHOT.jar >> api.log 2>&1 &
