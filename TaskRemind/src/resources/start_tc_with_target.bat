@echo off
rem 

setlocal

rem TPR is short for TC_PORTAL_ROOT to reduce command line length
title Teamcenter Rich Client

set ROOT_PATH=#{ROOT_PATH}

call %ROOT_PATH%\install\tem_init.bat

set TPR=%ROOT_PATH%/portal
if not defined FMS_HOME set FMS_HOME=%ROOT_PATH%/tccs

rem use AUX_PATH env var for any additional required paths
rem save original path for external applications
set ORIGINAL_PATH=%PATH%
rem for optimal startup performance, keep the PATH length at a minimum
set PATH=%SYSTEMROOT%\system32;%FMS_HOME%\bin;%FMS_HOME%\lib;%TPR%;%AUX_PATH%

set JAVA_HOME=%TC_JRE_HOME%
set JRE_HOME=%TC_JRE_HOME%



:start_portal
cd /d %TPR%
set CLASSPATH=.;
set VM_XMX=2048m

rem Set DJIPJL_VMARG environment variable
IF EXIST "%TPR%\djipjl\setenv.cmd" call "%TPR%\djipjl\setenv.cmd"


@echo Starting Teamcenter Rich Client...
"%JRE_HOME%\bin\javaw.exe" -jar plugins\sieUpdater.jar
start Teamcenter.exe %1 -vm "%JRE_HOME%\bin\javaw.exe" -vmargs -Xmx%VM_XMX% %DJIPJL_VMARG% -XX:MaxPermSize=128m -Xbootclasspath/a:"%JRE_HOME%\lib\plugin.jar";"%JRE_HOME%\lib\deploy.jar";"%JRE_HOME%\lib\javaws.jar" -Dautologin.user=%2 -Dautologin.pass=%3 %DJIPJL_VMARG%
