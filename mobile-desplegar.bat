@echo off
REM Script para desplegar la aplicación mobile en Tomcat

setlocal
set TOMCAT_HOME=C:\apache-tomcat-11.0.4
set WAR_FILE=%~dp0mobile\target\web.war
set WEBAPP_NAME=mobile

echo ========================================
echo  Desplegando aplicación Mobile en Tomcat
echo ========================================

REM Verificar si existe Tomcat
if not exist "%TOMCAT_HOME%" (
    echo ❌ No se encontró Tomcat en: %TOMCAT_HOME%
    echo.
    echo Por favor, ajusta la variable TOMCAT_HOME en este script
    echo o instala Tomcat 10.1
    pause
    exit /b 1
)

REM Verificar si existe el WAR
if not exist "%WAR_FILE%" (
    echo ❌ No se encontró el archivo WAR en: %WAR_FILE%
    echo.
    echo Ejecuta primero: mobile-compilar.bat
    pause
    exit /b 1
)

REM Detener Tomcat si está corriendo
echo Deteniendo Tomcat (si está corriendo)...
call "%TOMCAT_HOME%\bin\shutdown.bat" 2>nul
timeout /t 3 >nul

REM Limpiar despliegue anterior
if exist "%TOMCAT_HOME%\webapps\%WEBAPP_NAME%" (
    echo Eliminando despliegue anterior...
    rmdir /s /q "%TOMCAT_HOME%\webapps\%WEBAPP_NAME%"
)
if exist "%TOMCAT_HOME%\webapps\%WEBAPP_NAME%.war" (
    del /f /q "%TOMCAT_HOME%\webapps\%WEBAPP_NAME%.war"
)

REM Copiar nuevo WAR
echo Copiando WAR a Tomcat...
copy "%WAR_FILE%" "%TOMCAT_HOME%\webapps\%WEBAPP_NAME%.war"

REM Iniciar Tomcat
echo Iniciando Tomcat...
start "Tomcat Mobile" "%TOMCAT_HOME%\bin\startup.bat"

echo.
echo ========================================
echo  ✓ Despliegue completado
echo ========================================
echo.
echo La aplicación estará disponible en:
echo   http://localhost:8080/%WEBAPP_NAME%
echo.
echo Espera unos segundos para que Tomcat despliegue la aplicación
echo.
pause
