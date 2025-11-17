#!/bin/bash

# ==============================
# Configuración
# ==============================
CONFIG_FILE="$HOME/config/config.properties"

echo "========================================"
echo "Compilación EventosUy (Central + Web + Mobile)"
echo "========================================"
echo

if [ ! -f "$CONFIG_FILE" ]; then
    echo "⚠️ No se encontró el archivo de configuración en: $CONFIG_FILE"
    echo "Se sigue igual, pero sin mostrar info de endpoints."
else
    echo "Leyendo configuración desde: $CONFIG_FILE"
    
    CARGA_HOST=$(grep '^publicadorCargaDatos.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    CARGA_PORT=$(grep '^publicadorCargaDatos.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    CARGA_PATH=$(grep '^publicadorCargaDatos.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    USUARIO_HOST=$(grep '^publicadorUsuario.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    USUARIO_PORT=$(grep '^publicadorUsuario.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    USUARIO_PATH=$(grep '^publicadorUsuario.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    REGISTRO_HOST=$(grep '^publicadorRegistro.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    REGISTRO_PORT=$(grep '^publicadorRegistro.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    REGISTRO_PATH=$(grep '^publicadorRegistro.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    echo
    echo "Endpoints configurados (runtime, NO para wsimport):"
    echo "  CargaDatos : http://${CARGA_HOST}:${CARGA_PORT}${CARGA_PATH}"
    echo "  Usuario    : http://${USUARIO_HOST}:${USUARIO_PORT}${USUARIO_PATH}"
    echo "  Registro   : http://${REGISTRO_HOST}:${REGISTRO_PORT}${REGISTRO_PATH}"
    echo
fi

BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"

# ==============================
# 1. Compilar Servidor Central
# ==============================
echo "===================================="
echo "[1/3] Compilando Servidor Central..."
echo "===================================="

cd "$BASE_DIR/ServidorCentral" || { echo "Error al entrar a ServidorCentral"; exit 1; }

mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar Servidor Central."
    exit 1
fi

JAR_GUI="$PWD/target/servidor-gui.jar"
echo "✓ Servidor Central compilado:"
echo "  - JAR GUI: $JAR_GUI"
echo

# ==============================
# 2. Compilar Servidor Web
# ==============================
echo "===================================="
echo "[2/3] Compilando Servidor Web..."
echo "===================================="

cd "$BASE_DIR/servidor-web" || { echo "Error al entrar a servidor-web"; exit 1; }

# IMPORTANTE: sin wsimport, solo build normal
mvn clean package
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar Servidor Web."
    exit 1
fi

WAR_WEB="$PWD/target/web.war"
echo "✓ Servidor Web compilado:"
echo "  - WAR: $WAR_WEB"
echo

# ==============================
# 3. Compilar Mobile
# ==============================
echo "===================================="
echo "[3/3] Compilando Mobile..."
echo "===================================="

cd "$BASE_DIR/mobile" || { echo "Error al entrar a mobile"; exit 1; }

# SIN wsimport: simplemente build normal
mvn clean package
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar Mobile."
    exit 1
fi

WAR_MOBILE="$PWD/target/mobile.war"
echo "✓ Mobile compilado:"
echo "  - WAR: $WAR_MOBILE"
echo

# ==============================
# Resumen final
# ==============================
echo "========================================"
echo "✅ Compilación completada"
echo "========================================"
echo "Artefactos generados:"
echo "  - Servidor Central (GUI): $JAR_GUI"
echo "  - Servidor Web (WAR)    : $WAR_WEB"
echo "  - Mobile (WAR)          : $WAR_MOBILE"
echo
echo "Recordatorio:"
echo "  - Levantar Servidor Central (PC1):"
echo "      java -jar $JAR_GUI"
echo "  - Levantar Tomcat (PC2) con los WAR que copies a webapps/"
echo
