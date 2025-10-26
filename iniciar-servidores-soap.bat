@echo off
echo ========================================
echo Iniciando Servidores SOAP
echo ========================================
echo.

cd /d "%~dp0"

echo [1/2] Iniciando PublicadorControlador en puerto 9128...
start "PublicadorControlador" java -cp target\servidor.jar publicadores.PublicadorControlador

timeout /t 5 /nobreak > nul

echo [2/2] Iniciando PublicadorUsuario en puerto 9129...
start "PublicadorUsuario" java -cp target\servidor.jar publicadores.PublicadorUsuario

echo.
echo ========================================
echo Servidores SOAP iniciados
echo ========================================
echo PublicadorControlador: http://localhost:9128/publicador?wsdl
echo PublicadorUsuario: http://localhost:9129/publicadorUsuario?wsdl
echo.
echo Presiona cualquier tecla para salir...
pause > nul
