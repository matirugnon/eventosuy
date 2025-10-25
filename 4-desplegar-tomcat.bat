@echo off
REM Script para desplegar el WAR en Tomcat

echo ========================================
echo  Desplegando en Tomcat...
echo ========================================

set TOMCAT_DIR=C:\Program Files\Apache Software Foundation\Tomcat 11.0

echo.
echo Verificando que el WAR exista...
if not exist "servidor-web\target\web.war" (
    echo.
    echo ❌ Error: No se encuentra servidor-web\target\web.war
    echo    Primero debes compilar el proyecto con: .\2-compilar-webserver.bat
    pause
    exit /b 1
)

echo ✓ WAR encontrado
echo.

echo Copiando WAR a Tomcat...
copy servidor-web\target\web.war "%TOMCAT_DIR%\webapps\"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Error al copiar el WAR
    echo    Verifica que tienes permisos de administrador
    pause
    exit /b 1
)

echo ✓ WAR copiado exitosamente
echo.

echo ========================================
echo  Iniciando Tomcat...
echo ========================================
echo.

REM Configurar variables de entorno necesarias
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.8.9-hotspot
set CATALINA_HOME=%TOMCAT_DIR%

echo Configurando JAVA_HOME=%JAVA_HOME%
echo Configurando CATALINA_HOME=%CATALINA_HOME%
echo.

"%TOMCAT_DIR%\bin\startup.bat"

echo.
echo ========================================
echo  ✅ Despliegue completado
echo ========================================
echo.
echo Espera unos segundos para que Tomcat despliegue la aplicación
echo.
echo Luego accede a:
echo   - http://localhost:8080/web/         (Inicio de EventosUy)
echo   - http://localhost:8080/web/inicio   (Servlet de inicio)
echo   - http://localhost:8080/web/test-soap (Test SOAP)
echo.
pause
