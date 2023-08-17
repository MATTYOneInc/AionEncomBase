@echo off
TITLE Aion 5.8 - Game Server Console

SET PATH="..\JavaJDK_7\bin"

:START
CLS

echo.
echo Starting Aion Version 5.8 - Game Server.

echo.

REM -------------------------------------
REM Default parameters for a basic server.
java -Xms4096m -Xmx8192m -XX:MaxHeapSize=8192m -Xdebug -XX:MaxNewSize=48m -XX:NewSize=48m -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseConcMarkSweepGC -XX:-UseSplitVerifier -ea -javaagent:./libs/al-commons.jar -cp ./libs/*;./libs/AL-Game.jar com.aionemu.gameserver.GameServer
REM -------------------------------------
SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
if ERRORLEVEL 0 goto end

REM Restart...
:restart
echo.
echo Administrator Restart ...
echo.
goto start

REM Error...
:error
echo.
echo Server terminated abnormaly ...
echo.
goto end

REM End...
:end
echo.
echo Server terminated ...
echo.
pause