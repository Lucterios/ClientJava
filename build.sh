#!/bin/sh

svn checkout http://projets.lucterios.org/svn/Clients/java/ . --username=user --password=user123

rm -f *.zip
rm -f *.tar.gz
rm -f *.tar
rm -f *.lpk
rm -f Output.txt

./createSources.sh 

./compile.sh

cp antBuilderOutput.log bin/Output.txt