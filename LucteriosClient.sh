#!/bin/sh
### Launch Script for Client Lucterios

VA_APP_HOME=`dirname $0`
cd $VA_APP_HOME
java -Xmx64m -jar "$VA_APP_HOME/LucteriosClient.jar" $*
