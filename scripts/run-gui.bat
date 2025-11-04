@echo off
echo ====================================
echo Compilando Servidor Central...
echo ====================================

cd /d "%~dp0..\ServidorCentral"
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo Error al compilar el proyecto.
    pause
    exit /b %errorlevel%
)

echo.
echo ====================================
echo Iniciando GUI del Servidor Central...
echo ====================================

start java -jar target\servidor-gui.jar

echo GUI iniciada. Presiona cualquier tecla para cerrar esta ventana...
pause
