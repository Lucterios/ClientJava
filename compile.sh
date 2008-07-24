#! /bin/sh
# 

VersionMaj=0
VersionMin=35
VersionRev=5
current_date_sec=`date +%s`
VersionBuild=$(( (current_date_sec-1214866800)/7200 ))

clear

echo "*** Compilation $VersionMaj.$VersionMin.$VersionRev.$VersionBuild ***"
echo $VersionMaj $VersionMin $VersionRev $VersionBuild > version.txt

rm -f setup.inc.php
cp template_setup.inc.php setup.tmp
sed -e "s/\$version_max=X;/\$version_max=$VersionMaj;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_min=X;/\$version_min=$VersionMin;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_release=X;/\$version_release=$VersionRev;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_build=X;/\$version_build=$VersionBuild;/" setup.tmp > setup.inc.php

ArcFileName=JavaClient_$VersionMaj-$VersionMin-$VersionRev-$VersionBuild

rm Lucterios_$ArcFileName.zip
rm *.tar
rm JavaClient.zip

cp -vf version.txt LucteriosClient/org/lucterios/client/resources/version.txt

class_path=$PWD/COMPILEDIR/lib/LucteriosPrint.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/LucteriosUtils.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/commons-httpclient-3.0.1.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/commons-logging-1.0.4.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/commons-codec-1.3.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/xercesImpl.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/jdom.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/junit.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/js.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/fop.jar:$PWD/lib/ekit.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/batik-all.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/Date_selector.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/tableview.jar
class_path=$class_path:$PWD/COMPILEDIR/lib/avalon-framework-4.2.0.jar

export CLASSPATH=$class_path
export JAVA_HOME="/usr/lib/jvm/java-1.5.0-sun"

echo "----------------------------"

rm -vf Lucterios*.jar
rm -vf lib/Lucterios*.jar

rm -vfR COMPILEDIR/*
rmdir COMPILEDIR
mkdir COMPILEDIR
mkdir COMPILEDIR/lib
cp -vrf ./lib COMPILEDIR
cp -vf ./Makefile COMPILEDIR
unzip LucteriosUtils_Source -d COMPILEDIR
unzip LucteriosPrint_Source -d COMPILEDIR
unzip LucteriosClient_Source -d COMPILEDIR
unzip LucteriosUpdate_Source -d COMPILEDIR

cp -vf version.txt COMPILEDIR/LucteriosClient/org/lucterios/client/resources/version.txt

cd COMPILEDIR 
	make clean
	cd LucteriosUtils
		make all
		if [ $? -ne 0 ]
		then
			echo "+++ Error Utils +++"
			exit 11
		fi
	cd ..
	cp LucteriosUtils/LucteriosUtils.jar lib 
	cd LucteriosPrint
		make all
		if [ $? -ne 0 ]
		then
			echo "+++ Error Print +++"
			exit 12
		fi
	cd ..
	cp LucteriosPrint/LucteriosPrint.jar lib
	cd LucteriosClient
		make all
		if [ $? -ne 0 ]
		then
			echo "+++ Error Client +++"
			exit 13
		fi
	cd ..
	cd LucteriosUpdate
		make all
		if [ $? -ne 0 ]
		then
			echo "+++ Error Update +++"
			exit 14
		fi
	cd ..
cd ..

cp COMPILEDIR/lib/LucteriosUtils.jar lib 
cp COMPILEDIR/lib/LucteriosPrint.jar lib
cp COMPILEDIR/LucteriosClient.jar .
cp COMPILEDIR/LucteriosUpdate.jar .

zip Lucterios_$ArcFileName LucteriosClient.jar lib/* LucteriosClient.bat LucteriosClient.sh LucteriosLogo.png LucteriosLogo.ico setup.inc.php
if [ $? -ne 0 ]
then
	echo "+++ Error zip +++"
	exit 1
fi
zip JavaClient LucteriosClient.jar lib/*
if [ $? -ne 0 ]
then
	echo "+++ Error zip +++"
	exit 1
fi
tar -cf $ArcFileName.tar JavaClient.zip LucteriosUpdate.jar version.txt index.php LucteriosClient.bat LucteriosClient.sh LucteriosLogo.png LucteriosLogo.ico setup.inc.php
if [ $? -ne 0 ]
then
	echo "+++ Error lpk +++"
	exit 2
fi
cp $ArcFileName.tar java
gzip -f -S .lpk java
if [ $? -ne 0 ]
then
	echo "+++ Error lpk +++"
	exit 2
fi

rm -rf bin
mkdir bin
cp java.lpk bin/$ArcFileName.lpk
cp Lucterios_$ArcFileName.zip bin/Lucterios_$ArcFileName.zip

echo "--- Compilation $VersionMaj.$VersionMin.$VersionRev.$VersionBuild Success ---"
echo "----------------------------"
