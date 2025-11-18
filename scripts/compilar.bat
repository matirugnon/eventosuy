@echo off
setlocal EnableDelayedExpansion

for %%I in ("%~dp0..") do set "BASE_DIR=%%~fI"
set "CONFIG_FILE=%USERPROFILE%\config\config.properties"
if not exist "%CONFIG_FILE%" if exist "%BASE_DIR%\config\config.properties" (
    set "CONFIG_FILE=%BASE_DIR%\config\config.properties"
)

echo ========================================
echo Compilacion EventosUy ^(Central + Web + Mobile^)
echo ========================================
echo.

if exist "%CONFIG_FILE%" (
    echo Leyendo configuracion desde: "%CONFIG_FILE%"
    call :readProp "publicadorCargaDatos.host" CARGA_HOST
    call :readProp "publicadorCargaDatos.port" CARGA_PORT
    call :readProp "publicadorCargaDatos.url"  CARGA_PATH

    call :readProp "publicadorUsuario.host"    USUARIO_HOST
    call :readProp "publicadorUsuario.port"    USUARIO_PORT
    call :readProp "publicadorUsuario.url"     USUARIO_PATH

    call :readProp "publicadorRegistro.host"   REGISTRO_HOST
    call :readProp "publicadorRegistro.port"   REGISTRO_PORT
    call :readProp "publicadorRegistro.url"    REGISTRO_PATH

    call :readProp "publicadorControlador.host" CONTROLADOR_HOST
    call :readProp "publicadorControlador.port" CONTROLADOR_PORT
    call :readProp "publicadorControlador.url"  CONTROLADOR_PATH

    echo.
    echo Endpoints configurados ^(runtime, NO para wsimport^):
    if defined CARGA_HOST if defined CARGA_PORT if defined CARGA_PATH echo   CargaDatos  : http://!CARGA_HOST!:!CARGA_PORT!!CARGA_PATH!
    if defined CONTROLADOR_HOST if defined CONTROLADOR_PORT if defined CONTROLADOR_PATH echo   Controlador : http://!CONTROLADOR_HOST!:!CONTROLADOR_PORT!!CONTROLADOR_PATH!
    if defined REGISTRO_HOST if defined REGISTRO_PORT if defined REGISTRO_PATH echo   Registro    : http://!REGISTRO_HOST!:!REGISTRO_PORT!!REGISTRO_PATH!
    if defined USUARIO_HOST if defined USUARIO_PORT if defined USUARIO_PATH echo   Usuario     : http://!USUARIO_HOST!:!USUARIO_PORT!!USUARIO_PATH!
    echo.
) else (
    echo [WARN] No se encontro el archivo de configuracion en: "%CONFIG_FILE%"
    echo Continuando sin mostrar informacion de endpoints.
    echo.
)

:: ==============================
:: 1. Compilar Servidor Central
:: ==============================
echo ====================================
echo [1/3] Compilando Servidor Central...
echo ====================================

pushd "%BASE_DIR%\ServidorCentral" || goto :error_enter_sc
call mvn clean package -DskipTests
if errorlevel 1 goto :error_sc
set "JAR_GUI=%CD%\target\servidor-gui.jar"
popd
echo [OK] Servidor Central compilado:
echo    - JAR GUI: %JAR_GUI%
echo.

:: ==============================
:: 2. Compilar Servidor Web
:: ==============================
echo ====================================
echo [2/3] Compilando Servidor Web...
echo ====================================

pushd "%BASE_DIR%\servidor-web" || goto :error_enter_web
call mvn clean package
if errorlevel 1 goto :error_web
set "WAR_WEB=%CD%\target\web.war"
popd
echo [OK] Servidor Web compilado:
echo    - WAR: %WAR_WEB%
echo.

:: ==============================
:: 3. Compilar Mobile
:: ==============================
echo ====================================
echo [3/3] Compilando Mobile...
echo ====================================

pushd "%BASE_DIR%\mobile" || goto :error_enter_mobile
call mvn clean package
if errorlevel 1 goto :error_mobile
set "WAR_MOBILE=%CD%\target\mobile.war"
popd
echo [OK] Mobile compilado:
echo    - WAR: %WAR_MOBILE%
echo.

echo ========================================
echo [OK] Compilacion completada
echo ========================================
echo Artefactos generados:
echo   - Servidor Central ^(GUI^): %JAR_GUI%
echo   - Servidor Web ^(WAR^)   : %WAR_WEB%
echo   - Mobile ^(WAR^)         : %WAR_MOBILE%
echo.
echo Recordatorio:
echo   - Levantar Servidor Central:
echo       java -jar "%JAR_GUI%"
echo   - Copiar los WAR a webapps/ del Tomcat que corresponda

exit /b 0

:error_enter_sc
echo [ERR] No se pudo acceder a %BASE_DIR%\ServidorCentral
exit /b 1

:error_sc
popd >nul
echo [ERR] Error al compilar Servidor Central.
exit /b 1

:error_enter_web
echo [ERR] No se pudo acceder a %BASE_DIR%\servidor-web
exit /b 1

:error_web
popd >nul
echo [ERR] Error al compilar Servidor Web.
exit /b 1

:error_enter_mobile
echo [ERR] No se pudo acceder a %BASE_DIR%\mobile
exit /b 1

:error_mobile
popd >nul
echo [ERR] Error al compilar Mobile.
exit /b 1

:return
exit /b 0

:readProp
setlocal EnableDelayedExpansion
set "searchKey=%~1"
set "result="
for /f "usebackq tokens=1* eol=# delims==" %%A in ("%CONFIG_FILE%") do (
    set "key=%%A"
    set "value=%%B"
    if /i "!key!"=="%searchKey%" (
        set "result=!value!"
    )
)
endlocal & set "%~2=%result%"
exit /b 0
