@echo off
:: run-gui.bat - Arranca la GUI empaquetada en target\servidor-gui.jar
:: Ubica este archivo en el directorio del módulo ServidorCentral.
chcp 65001 >nul
set SCRIPT_DIR=%~dp0
set JAR=%SCRIPT_DIR%target\servidor-gui.jar
if not exist "%JAR%" (
  echo ERROR: No se encontró el JAR: %JAR%
  echo Compilar con Maven: mvn -f "%SCRIPT_DIR%pom.xml" -DskipTests package
  pause
  exit /b 1
)
echo Iniciando GUI desde: %JAR%
java -jar "%JAR%"
exit /b %ERRORLEVEL%
