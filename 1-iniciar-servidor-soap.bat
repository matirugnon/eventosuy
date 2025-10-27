@echo off
echo ========================================
echo Iniciando Servidores SOAP
echo ========================================
echo.

cd /d "%~dp0"

cd ServidorCentral

echo Iniciando los tres servicios en un solo proceso...
echo - PublicadorControlador en puerto 9128
echo - PublicadorUsuario en puerto 9129
echo - PublicadorRegistro en puerto 9130
echo.
java -jar target\servidor.jar

pause
