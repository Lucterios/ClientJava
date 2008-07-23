#!/bin/sh

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
