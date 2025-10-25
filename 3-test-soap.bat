@echo off
REM Script para probar la conexión SOAP

echo ========================================
echo  Test de Conexión SOAP
echo ========================================
echo.
echo Este script abrirá el navegador para probar la conexión SOAP
echo.
echo Asegúrate de que:
echo 1. El Servidor Central esté corriendo
echo 2. El Servidor Web esté desplegado en Tomcat
echo.
pause

start http://localhost:8080/web/test-soap

echo.
echo ✓ Navegador abierto
echo.
echo Deberías ver:
echo   ✓ Servidor SOAP disponible
echo   ✓ Respuesta del método hola()
echo   ✓ Lista de eventos
echo.
pause
