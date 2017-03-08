#!/bin/bash

cd ..

git clone https://github.com/diging/giles-eco-requests.git
cd giles-eco-requests && mvn install -Dgeco.requests.version=v0.1

cd ..
git clone https://github.com/diging/giles-eco-util.git
cd util && mvn install -Dgeco.util.version=v0.4.2-SNAPSHOT

cd ../hank && mvn test -Dgeco.util.version=v0.4.2-SNAPSHOT