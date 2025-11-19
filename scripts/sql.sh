#!/bin/bash

CONFIG_FILE="$HOME/config/config.properties"

if [ ! -f "$CONFIG_FILE" ]; then
    echo "[ERROR] No se encontró el archivo de configuración: $CONFIG_FILE"
    exit 1
fi

echo "Leyendo configuración desde: $CONFIG_FILE"

DB_USER=$(grep '^hsqldb.user='     "$CONFIG_FILE" | cut -d'=' -f2-)
DB_PASS=$(grep '^hsqldb.password=' "$CONFIG_FILE" | cut -d'=' -f2-)
HSQLDB_JAR=$(grep '^hsqldb.jar='      "$CONFIG_FILE" | cut -d'=' -f2-)
DB_PATH=$(grep '^hsqldb.db_path='     "$CONFIG_FILE" | cut -d'=' -f2-)
DB_NAME=$(grep '^hsqldb.db_name='     "$CONFIG_FILE" | cut -d'=' -f2-)
HSQL_PORT=$(grep '^hsqldb.port='      "$CONFIG_FILE" | cut -d'=' -f2-)
HSQL_ADDR=$(grep '^hsqldb.address='   "$CONFIG_FILE" | cut -d'=' -f2-)

if [ ! -f "$HSQLDB_JAR" ]; then
    echo "[ERROR] El JAR de HSQLDB no existe: $HSQLDB_JAR"
    exit 1
fi

echo ""
echo "========================================"
echo "Configuración HSQLDB"
echo "========================================"
echo "JAR      : $HSQLDB_JAR"
echo "DB Path  : $DB_PATH"
echo "DB Name  : $DB_NAME"
echo "Port     : $HSQL_PORT"
echo "Address  : $HSQL_ADDR"
echo "User     : $DB_USER"
echo "========================================"
echo ""

echo "[1] Iniciando servidor HSQLDB..."
java -cp "$HSQLDB_JAR" org.hsqldb.server.Server \
  --database.0 "file:${DB_PATH}" \
  --dbname.0   "$DB_NAME" \
  --port       "$HSQL_PORT" \
  --address    "$HSQL_ADDR" &

HSQL_PID=$!
echo "Servidor HSQLDB iniciado (PID: $HSQL_PID)"

sleep 3

echo "[2] Abriendo Database Manager GUI..."
java -cp "$HSQLDB_JAR" org.hsqldb.util.DatabaseManagerSwing \
  --url "jdbc:hsqldb:hsql://${HSQL_ADDR}:${HSQL_PORT}/${DB_NAME}" \
  --user "$DB_USER" \
  --password "$DB_PASS" &

echo ""
echo "========================================"
echo "Servidor HSQLDB iniciado correctamente"
echo "========================================"
echo ""
echo "Para detenerlo: kill $HSQL_PID"
echo ""
