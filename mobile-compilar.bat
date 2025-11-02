@echo off
REM Script para compilar y preparar la aplicación mobile

echo ========================================
echo  Compilando aplicación Mobile
echo ========================================

cd /d "%~dp0mobile"

echo Regenerando clases SOAP desde WSDL...
call mvn clean jaxws:wsimport
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al regenerar el cliente SOAP
    echo    Asegúrate de que el Servidor Central está corriendo en http://localhost:9128
    pause
    exit /b 1
)

echo.
echo Compilando proyecto...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al compilar el proyecto
    pause
    exit /b 1
)

echo.
echo ✓ Compilación exitosa
echo El archivo WAR está en: mobile\target\web.war
echo.
pause
