#! /bin/bash

rm -rf src/**/*.class
rm -rf src/server.jar
rm -f public_html/classes/client/*.class
cd src

javac server/PresenceService.java
jar cvf server.jar server/*.class
cp server.jar ../public_html/classes

cd ..
javac -cp src src/engine/Engine.java
javac -cp src src/client/Client.java
javac -cp src src/server/PresenceService.java src/server/RegistrationInfo.java

cd src
cp client/Client.class ../public_html/classes/client

cd ..
java -cp src:public_html/classes/server.jar -Djava.rmi.server.codebase=http://localhost/classes -Djava.security.policy=src/client/client.policy -Djava.rmi.server.useCodebaseOnly=false client.Client $1 $2 $3
#java -cp src:src/server.jar -Djava.rmi.server.codebase=http://localhost/classes -Djava.security.policy=src/client/client.policy -Djava.rmi.server.useCodebaseOnly=false client.Client localhost
