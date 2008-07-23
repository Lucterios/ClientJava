#!/bin/sh

svn checkout http://projets.lucterios.org/svn/Clients/java/ . --username=user --password=user123

rm -f *.zip
rm -f *.tar.gz
rm -f *.tar
rm -f *.lpk
rm -fr BIN
mkdir BIN 

./createSources.sh 

./compile.sh

cp *.lpk BIN
mv *_JavaClient_*.zip BIN
