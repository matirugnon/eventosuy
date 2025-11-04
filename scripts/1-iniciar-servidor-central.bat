@echo off
REM Script para compilar y ejecutar el Servidor Central

echo ========================================
echo  Compilando Servidor Central...
echo ========================================

cd ServidorCentral

REM Intentar compilar sin clean (por si target está bloqueado)
call mvn package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Intentando de nuevo con clean...
    call mvn clean package -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo ❌ Error al compilar el Servidor Central
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo  Iniciando Servidor Central...
echo ========================================
echo.
echo PublicadorControlador: http://localhost:9128/publicador
echo PublicadorUsuario: http://localhost:9128/publicadorUsuario
echo.
echo WSDL PublicadorControlador: http://localhost:9128/publicador?wsdl
echo WSDL PublicadorUsuario: http://localhost:9128/publicadorUsuario?wsdl
echo.
echo Presiona Ctrl+C para detener el servidor
echo.

cd target
java -jar servidor.jar

pause
