@echo off
REM Ejecuta: (1) Servidor Central, (2) Regenerar cliente SOAP y compilar Web, (3) Desplegar en Tomcat


setlocal ENABLEDELAYEDEXPANSION
set ROOT_DIR=%~dp0
pushd "%ROOT_DIR%"

echo ========================================
echo  Paso 1/3: Iniciando Servidor Central
echo ========================================

REM Abrir el servidor central en una nueva ventana para no bloquear este script
if exist "1-iniciar-servidor-central.bat" (
    echo Abriendo ventana separada para el Servidor Central...
    start "Servidor Central" cmd /c "1-iniciar-servidor-central.bat"
) else (
    echo ❌ No se encontro 1-iniciar-servidor-central.bat
    exit /b 1
)



REM Paso 2: Esperar 10 segundos antes de compilar el web
echo.
echo ========================================
echo  Paso 2/3: Esperando 10 segundos para que el Servidor Central levante...
echo ========================================
timeout /t 15 >nul
echo.
echo ========================================
echo  Paso 2/3: Regenerar cliente SOAP y compilar servidor web
echo ========================================

if not exist "servidor-web" (
    echo ❌ No se encontro la carpeta servidor-web
    exit /b 1
)

pushd servidor-web

echo Regenerando clases SOAP desde WSDL...
call mvn clean jaxws:wsimport
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al regenerar el cliente SOAP (wsimport)
    echo    Asegurate de que el Servidor Central esta corriendo correctamente
    popd
    exit /b 1
)

echo Compilando y empaquetando WAR...
call mvn package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al compilar el Servidor Web
    popd
    exit /b 1
)

popd

echo.
echo ========================================
echo  Paso 3/3: Desplegar WAR en Tomcat e iniciar
echo ========================================

set TOMCAT_DIR=C:\Program Files\Apache Software Foundation\Tomcat 11.0
set WAR_PATH=servidor-web\target\web.war

if not exist "%WAR_PATH%" (
    echo ❌ No se encontro %WAR_PATH%
    echo    Revisa el paso 2/3 (compilacion)
    exit /b 1
)

echo Copiando WAR a %TOMCAT_DIR%\webapps\ ...
copy "%WAR_PATH%" "%TOMCAT_DIR%\webapps\" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error al copiar el WAR a Tomcat
    echo    Ejecuta esta ventana como Administrador si es necesario
    exit /b 1
)

echo Iniciando Tomcat...
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.8.9-hotspot
set CATALINA_HOME=%TOMCAT_DIR%

"%TOMCAT_DIR%\bin\startup.bat"

echo.
echo ========================================
echo  ✅ Todo listo
echo ========================================
echo URLs:
echo   - http://localhost:8080/web/
echo   - http://localhost:8080/web/inicio
echo   - http://localhost:8080/web/test-soap

echo.
echo NOTA: La ventana del Servidor Central quedo abierta aparte.
echo       Para detenerla, cierrala manualmente.

popd
endlocal

exit /b 0
