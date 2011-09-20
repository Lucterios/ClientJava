#!/bin/sh

echo "createSetup : $5/setup.inc.php"

rm -f $5/setup.inc.php
cp $5/template_setup.inc.php $5/setup.tmp
sed -e "s/\$version_max=X;/\$version_max=$1;/" $5/setup.tmp > $5/setup.inc.php
cp $5/setup.inc.php $5/setup.tmp
sed -e "s/\$version_min=X;/\$version_min=$2;/" $5/setup.tmp > $5/setup.inc.php
cp $5/setup.inc.php $5/setup.tmp
sed -e "s/\$version_release=X;/\$version_release=$3;/" $5/setup.tmp > $5/setup.inc.php
cp $5/setup.inc.php $5/setup.tmp
sed -e "s/\$version_build=X;/\$version_build=$4;/" $5/setup.tmp > $5/setup.inc.php
rm -f $5/setup.tmp
 
