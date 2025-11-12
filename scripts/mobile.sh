#!/bin/bash

# Ruta del archivo de configuración
CONFIG_FILE="/ens/home01/f/facundo.grela/config/config.properties"

# Valores por defecto (si no existe el config)
DEF_HOST="localhost"
DEF_PORT="9128"
DEF_TOMCAT_DIR="/opt/tomcat"

if [ ! -f "$CONFIG_FILE" ]; then
    echo "⚠️ No se encontró el archivo de configuración en: $CONFIG_FILE"
    echo "Usando valores por defecto."

    CARGA_HOST="$DEF_HOST"
    CARGA_PORT="$DEF_PORT"
    CARGA_PATH="/publicadorCargaDatos"

    USUARIO_HOST="$DEF_HOST"
    USUARIO_PORT="$DEF_PORT"
    USUARIO_PATH="/publicadorUsuario"

    REGISTRO_HOST="$DEF_HOST"
    REGISTRO_PORT="$DEF_PORT"
    REGISTRO_PATH="/publicadorRegistro"

    TOMCAT_DIR="$DEF_TOMCAT_DIR"
else
    # CargaDatos
    CARGA_HOST=$(grep '^publicadorCargaDatos.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    CARGA_PORT=$(grep '^publicadorCargaDatos.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    CARGA_PATH=$(grep '^publicadorCargaDatos.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    # Usuario
    USUARIO_HOST=$(grep '^publicadorUsuario.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    USUARIO_PORT=$(grep '^publicadorUsuario.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    USUARIO_PATH=$(grep '^publicadorUsuario.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    # Registro
    REGISTRO_HOST=$(grep '^publicadorRegistro.host=' "$CONFIG_FILE" | cut -d'=' -f2-)
    REGISTRO_PORT=$(grep '^publicadorRegistro.port=' "$CONFIG_FILE" | cut -d'=' -f2-)
    REGISTRO_PATH=$(grep '^publicadorRegistro.url='  "$CONFIG_FILE" | cut -d'=' -f2-)

    TOMCAT_DIR=$(grep '^tomcat.dir=' "$CONFIG_FILE" | cut -d'=' -f2-)

    # Fallbacks si algo viene vacío
    [ -z "$CARGA_HOST" ]    && CARGA_HOST="$DEF_HOST"
    [ -z "$CARGA_PORT" ]    && CARGA_PORT="$DEF_PORT"
    [ -z "$USUARIO_HOST" ]  && USUARIO_HOST="$DEF_HOST"
    [ -z "$USUARIO_PORT" ]  && USUARIO_PORT="$DEF_PORT"
    [ -z "$REGISTRO_HOST" ] && REGISTRO_HOST="$DEF_HOST"
    [ -z "$REGISTRO_PORT" ] && REGISTRO_PORT="$DEF_PORT"
    [ -z "$TOMCAT_DIR" ]    && TOMCAT_DIR="$DEF_TOMCAT_DIR"
fi

# Construir URLs completas de los WSDL
WSDL_CARGA="http://${CARGA_HOST}:${CARGA_PORT}${CARGA_PATH}?wsdl"
WSDL_USUARIO="http://${USUARIO_HOST}:${USUARIO_PORT}${USUARIO_PATH}?wsdl"
WSDL_REGISTRO="http://${REGISTRO_HOST}:${REGISTRO_PORT}${REGISTRO_PATH}?wsdl"

# Verificar Tomcat
if [ ! -d "$TOMCAT_DIR/webapps" ]; then
    echo "❌ Error: No se encontró el directorio webapps en Tomcat"
    echo "    Verifica que la ruta de Tomcat esté correctamente configurada en $CONFIG_FILE (tomcat.dir=...)"
    exit 1
fi

echo "========================================"
echo "Regenerando Cliente SOAP + compilando..."
echo "========================================"
echo
echo "IMPORTANTE: El Servidor Central debe estar corriendo en:"
echo " - $WSDL_CARGA"
echo " - $WSDL_USUARIO"
echo " - $WSDL_REGISTRO"
echo
read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."

# Cambiar al directorio de la aplicación móvil
cd "$(dirname "$0")/../mobile" || { echo "Error al cambiar de directorio"; exit 1; }

echo
echo "Ejecutando mvn clean package (wsimport + compilación)..."

mvn clean package \
    -Dwsdl.publicadorCargaDatos="$WSDL_CARGA" \
    -Dwsdl.publicadorUsuario="$WSDL_USUARIO" \
    -Dwsdl.publicadorRegistro="$WSDL_REGISTRO"

if [ $? -ne 0 ]; then
    echo
    echo "❌ Error al compilar la Aplicación Móvil / regenerar cliente SOAP"
    echo "    Asegúrate de que el Servidor Central esté corriendo en las URLs mostradas arriba."
    read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."
    exit 1
fi

echo
echo "========================================"
echo "✅ Compilación exitosa de la Aplicación Móvil"
echo "========================================"
echo
echo "El archivo WAR está en: /ens/devel01/tpgr15/tpgr15/mobile/target/mobile.war"
echo

# Despliegue en Tomcat (igual que antes)
echo "========================================"
echo "Desplegando en Tomcat..."
echo "========================================"

WAR_MOBILE_PATH="/ens/devel01/tpgr15/tpgr15/mobile/target/mobile.war"
if [ ! -f "$WAR_MOBILE_PATH" ]; then
    echo "❌ Error: No se encuentra el archivo WAR de la aplicación móvil en: $WAR_MOBILE_PATH"
    exit 1
fi

echo "Copiando WAR de la aplicación móvil a Tomcat..."
cp "$WAR_MOBILE_PATH" "$TOMCAT_DIR/webapps/"
if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Error al copiar el WAR de la aplicación móvil"
    echo "    Verifica que tienes permisos de escritura en $TOMCAT_DIR/webapps/"
    exit 1	
fi

echo "✓ WAR móvil copiado exitosamente"
echo ""

echo "========================================"
echo "Iniciando Tomcat..."
echo "========================================"

export JAVA_HOME="/usr/lib/jvm/java-21-openjdk"
export CATALINA_HOME="$TOMCAT_DIR"

echo "Configurando JAVA_HOME=$JAVA_HOME"
echo "Configurando CATALINA_HOME=$CATALINA_HOME"
echo ""

"$TOMCAT_DIR/bin/startup.sh"
if [ $? -ne 0 ]; then
    echo "❌ Error al iniciar Tomcat. Revisa los logs de Tomcat para más detalles."
    exit 1
fi

echo "========================================"
echo "✅ Despliegue completado"
echo "========================================"
echo ""
echo "Espera unos segundos para que Tomcat despliegue la aplicación"
echo ""
echo "Luego accede a:"
echo "   - http://localhost:8080/web_mobile/"
echo ""
