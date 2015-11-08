#!/bin/bash

function install {
  echo "installing $1 dependencies"
  cd $1 && mvn clean install -DskipTests && cd ..
}

install pinkelephant-commons && install pinkelephant-services &&  
cd pinkelephant-api && mvn clean install -DskipTests -Djava.security.egd=file:/dev/./urandom
