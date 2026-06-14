@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Usando Java:
java -version
echo.

cd /d "%~dp0"
call mvnw.cmd spring-boot:run
