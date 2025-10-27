@echo off
REM Script para regenerar el cliente SOAP y compilar el servidor web

echo ========================================
echo  Regenerando Cliente SOAP...
echo ========================================
echo.
echo IMPORTANTE: El Servidor Central debe estar corriendo
echo en http://localhost:9128/publicador
echo en http://localhost:9129/publicadorUsuario
echo en http://localhost:9130/publicadorRegistro
echo.
pause

cd servidor-web

echo.
echo Regenerando clases SOAP desde WSDL...
call mvn clean jaxws:wsimport

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al regenerar el cliente SOAP
    echo    Verifica que el Servidor Central esté corriendo
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Compilando Servidor Web...
echo ========================================

call mvn package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al compilar el Servidor Web
    pause
    exit /b 1
)

echo.
echo ========================================
echo  ✅ Compilación exitosa
echo ========================================
echo.
echo El archivo WAR está en: servidor-web\target\web.war
echo.
echo Para desplegar, ejecuta:
echo copy servidor-web\target\web.war "C:\Program Files\Apache Software Foundation\Tomcat 11.0\webapps\"
echo.
echo O ejecuta el script: 4-desplegar-tomcat.bat
echo.
pause
