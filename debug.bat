#!/bin/bash
SET SOURCE_PATH="C:\Users\James\OneDrive\Create\Mobile\Patron\Project\src\java"
SET DEBUG_PORT=8700
SET PACKAGE_NAME=me.endeal.patron.main
SET ACTIVITY=me.endeal.patron.main.FlashLogin
adb shell am start -e debug true -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n %PACKAGE_NAME%/%ACTIVITY%
start "" get_app_ports.bat
TIMEOUT 3
for /f "tokens=2 delims=," %%F in ('tasklist /nh /fi "imagename eq adb.exe" /fo csv') do @set poop=%%~F
taskkill /F /PID %poop%
taskkill /fi "imagename eq cmd.exe" /fi "windowtitle eq get_app_ports.bat"
cat app_debug_ports.txt | tail -1 > app_debug_port.txt
SET /p APP_DEBUG_PORT=<app_debug_port.txt
adb forward tcp:%DEBUG_PORT% jdwp:%APP_DEBUG_PORT%
jdb –sourcepath %SOURCE_PATH% -connect com.sun.jdi.SocketAttach:hostname=localhost,port=%DEBUG_PORT%