@echo off
REM Abrir puertos en el Firewall para acceso desde dispositivos móviles
REM EJECUTAR COMO ADMINISTRADOR (Click derecho -> Ejecutar como administrador)

echo ========================================
echo  Configurando Firewall de Windows
echo ========================================
echo.
echo NOTA: Debes ejecutar este script como Administrador
echo.

REM Puerto 9128 para Servidor Central
echo [1/2] Abriendo puerto 9128 (Servidor Central SOAP)...
netsh advfirewall firewall add rule name="Servidor Central SOAP 9128" dir=in action=allow protocol=TCP localport=9128
if %ERRORLEVEL% EQU 0 (
    echo ✓ Puerto 9128 abierto correctamente
) else (
    echo ❌ Error al abrir puerto 9128
    echo    ¿Ejecutaste este script como Administrador?
)

echo.

REM Puerto 8080 para Tomcat
echo [2/2] Abriendo puerto 8080 (Tomcat)...
netsh advfirewall firewall add rule name="Tomcat 8080" dir=in action=allow protocol=TCP localport=8080
if %ERRORLEVEL% EQU 0 (
    echo ✓ Puerto 8080 abierto correctamente
) else (
    echo ❌ Error al abrir puerto 8080
)

echo.
echo ========================================
echo  Configuración completada
echo ========================================
echo.
echo Los puertos 9128 y 8080 ahora están abiertos para conexiones entrantes.
echo.
pause
