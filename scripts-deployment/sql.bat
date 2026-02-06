@echo off
setlocal EnableDelayedExpansion

set "CONFIG_FILE=%USERPROFILE%\config\config.properties"

if not exist "%CONFIG_FILE%" (
    echo [ERROR] No se encontro el archivo de configuracion: %CONFIG_FILE%
    pause
    exit /b 1
)

echo Leyendo configuracion desde: %CONFIG_FILE%

call :readProp "hsqldb.jar" HSQLDB_JAR
call :readProp "hsqldb.db_path" DB_PATH
call :readProp "hsqldb.db_name" DB_NAME
call :readProp "hsqldb.port" HSQL_PORT
call :readProp "hsqldb.address" HSQL_ADDR
call :readProp "hsqldb.user" DB_USER
call :readProp "hsqldb.password" DB_PASS

if not defined HSQLDB_JAR (
    echo [ERROR] No se encontro la propiedad 'hsqldb.jar' en el archivo de configuracion
    pause
    exit /b 1
)

if not exist "%HSQLDB_JAR%" (
    echo [ERROR] El JAR de HSQLDB no existe: %HSQLDB_JAR%
    pause
    exit /b 1
)

echo.
echo ========================================
echo Configuracion HSQLDB
echo ========================================
echo JAR      : %HSQLDB_JAR%
echo DB Path  : %DB_PATH%
echo DB Name  : %DB_NAME%
echo Port     : %HSQL_PORT%
echo Address  : %HSQL_ADDR%
echo User     : %DB_USER%
echo ========================================
echo.

echo [1] Iniciando servidor HSQLDB...
start "HSQLDB Server" java -cp "%HSQLDB_JAR%" org.hsqldb.server.Server ^
    --database.0 "file:%DB_PATH%" ^
    --dbname.0 "%DB_NAME%" ^
    --port %HSQL_PORT% ^
    --address %HSQL_ADDR%

timeout /t 3 /nobreak >nul

echo [2] Abriendo Database Manager GUI...
start "HSQLDB Manager" java -cp "%HSQLDB_JAR%" org.hsqldb.util.DatabaseManagerSwing ^
    --url "jdbc:hsqldb:hsql://%HSQL_ADDR%:%HSQL_PORT%/%DB_NAME%" ^
    --user "%DB_USER%" ^
    --password "%DB_PASS%"

echo.
echo ========================================
echo Servidor HSQLDB iniciado correctamente
echo ========================================
echo.
echo Para detenerlo, cierra la ventana "HSQLDB Server"
echo o ejecuta: taskkill /FI "WINDOWTITLE eq HSQLDB Server*"
echo.

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
