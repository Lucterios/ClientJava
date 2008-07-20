
clear

echo "**** Create ZIP SOURCE ***"

rm LucteriosUtils_Source.zip
rm LucteriosPrint_Source.zip
rm LucteriosClient_Source.zip
rm LucteriosUpdate_Source.zip

zip -r LucteriosClient_Source LucteriosClient/Makefile LucteriosClient/Makefile.include LucteriosClient/Manifest.txt LucteriosClient/org/ -x *.class
zip -r LucteriosPrint_Source LucteriosPrint/Makefile LucteriosPrint/Makefile.include LucteriosPrint/Manifest.txt LucteriosPrint/org/ -x *.class
zip -r LucteriosUtils_Source LucteriosUtils/Makefile LucteriosUtils/Makefile.include LucteriosUtils/Manifest.txt LucteriosUtils/org/ -x *.class
zip -r LucteriosUpdate_Source LucteriosUpdate/Makefile LucteriosUpdate/Makefile.include LucteriosUpdate/Manifest.txt LucteriosUpdate/org/ -x *.class

echo "----------------------------"
