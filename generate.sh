#!/bin/bash
glogin=
gpassword=
targetfolder=/tmp/gen

mkdir -p $targetfolder
java -Xmx400M -Dfile.encoding=ISO-8859-1 -cp target/generators-1.0-SNAPSHOT-jar-with-dependencies.jar org.berlinvegan.generators.ListGenerator -u $glogin -p $gpassword -o $targetfolder
java -Xmx400M -Dfile.encoding=ISO-8859-1 -cp target/generators-1.0-SNAPSHOT-jar-with-dependencies.jar org.berlinvegan.generators.FactsheetGenerator -u $glogin -p $gpassword -o $targetfolder
java -Xmx400M -Dfile.encoding=ISO-8859-1 -cp target/generators-1.0-SNAPSHOT-jar-with-dependencies.jar org.berlinvegan.generators.MapGenerator -u $glogin -p $gpassword -o $targetfolder
