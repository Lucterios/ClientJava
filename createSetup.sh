#!/bin/sh

rm -f setup.inc.php
cp template_setup.inc.php setup.tmp
sed -e "s/\$version_max=X;/\$version_max=$1;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_min=X;/\$version_min=$2;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_release=X;/\$version_release=$3;/" setup.tmp > setup.inc.php
cp setup.inc.php setup.tmp
sed -e "s/\$version_build=X;/\$version_build=$4;/" setup.tmp > setup.inc.php
 
