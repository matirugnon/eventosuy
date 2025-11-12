#!/bin/bash

# Ruta del archivo de configuración
CONFIG_FILE="/ens/home01/f/facundo.grela/config/config.properties"

# Verificar si el archivo de configuración existe
if [ ! -f "$CONFIG_FILE" ]; then
    echo "⚠️ No se encontró el archivo de configuración en: $CONFIG_FILE"
    echo "Usando valores por defecto."
    PUBLICADOR1_URL="http://localhost:9128/publicador"
    PUBLICADOR2_URL="http://localhost:9129/publicadorUsuario"
    PUBLICADOR3_URL="http://localhost:9130/publicadorRegistro"
    PUBLICADOR4_URL="http://localhost:9131/publicadorUsuario"
    TOMCAT_DIR="/opt/tomcat"  # Valor por defecto para Tomcat
else
    PUBLICADOR1_URL=$(grep 'publicadorCargaDatos.url' "$CONFIG_FILE" | cut -d'=' -f2)
    PUBLICADOR2_URL=$(grep 'publicadorControlador.url' "$CONFIG_FILE" | cut -d'=' -f2)
    PUBLICADOR3_URL=$(grep 'publicadorRegistro.url' "$CONFIG_FILE" | cut -d'=' -f2)
    PUBLICADOR4_URL=$(grep 'publicadorUsuario.url' "$CONFIG_FILE" | cut -d'=' -f2)
    TOMCAT_DIR=$(grep 'tomcat.dir' "$CONFIG_FILE" | cut -d'=' -f2)
fi

# Verificar si la ruta de Tomcat existe
if [ ! -d "$TOMCAT_DIR/webapps" ]; then
    echo "❌ Error: No se encontró el directorio webapps en Tomcat"
    echo "    Verifica que la ruta de Tomcat esté correctamente configurada en $CONFIG_FILE"
    exit 1
fi

# Imprimir las URLs de los publicadores
echo "========================================"
echo "Regenerando Cliente SOAP..."
echo "========================================"
echo
echo "IMPORTANTE: El Servidor Central debe estar corriendo"
echo "en $PUBLICADOR1_URL"
echo "en $PUBLICADOR2_URL"
echo "en $PUBLICADOR3_URL"
echo "en $PUBLICADOR4_URL"
echo
read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."

# Cambiar al directorio del servidor web
cd "$(dirname "$0")/../servidor-web" || { echo "Error al cambiar de directorio"; exit 1; }

echo
echo "Regenerando clases SOAP desde WSDL..."
# Ejecutar Maven para regenerar el cliente SOAP
mvn clean jaxws:wsimport

# Verificar si Maven tuvo éxito
if [ $? -ne 0 ]; then
    echo
    echo "❌ Error al regenerar el cliente SOAP"
    echo "Verifica que el Servidor Central esté corriendo"
    read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."
    exit 1
fi

echo
echo "========================================"
echo "Compilando Servidor Web..."
echo "========================================"

# Ejecutar Maven para compilar el servidor web
mvn package

# Verificar si Maven tuvo éxito
if [ $? -ne 0 ]; then
    echo
    echo "❌ Error al compilar el Servidor Web"
    read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."
    exit 1
fi

echo
echo "========================================"
echo "✅ Compilación exitosa"
echo "========================================"
echo
echo "El archivo WAR está en: /ens/devel01/tpgr15/tpgr15/servidor-web/target/web.war"
echo
echo "Para desplegar, ejecuta:"
echo "cp /ens/devel01/tpgr15/tpgr15/servidor-web/target/web.war $TOMCAT_DIR/webapps/"
echo
echo "O ejecuta el script: 4-desplegar-tomcat.sh"
echo
read -n 1 -s -r -p "Presiona cualquier tecla para continuar..."

# Script para desplegar el WAR en Tomcat
echo "========================================"
echo " Desplegando en Tomcat..."
echo "========================================"

# Verificar que el archivo WAR existe en la ruta correcta
WAR_PATH="/ens/devel01/tpgr15/tpgr15/servidor-web/target/web.war"

if [ ! -f "$WAR_PATH" ]; then
    echo ""
    echo "❌ Error: No se encuentra $WAR_PATH"
    echo "    Primero debes compilar el proyecto con: ./2-compilar-webserver.sh"
    exit 1
fi

echo "✓ WAR encontrado"
echo ""

# Copiar el WAR al directorio de Tomcat
echo "Copiando WAR a Tomcat..."
cp "$WAR_PATH" "$TOMCAT_DIR/webapps/"

# Verificar si la copia fue exitosa
if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Error al copiar el WAR"
    echo "    Verifica que tienes permisos de escritura en $TOMCAT_DIR/webapps/"
    exit 1	
fi

echo "✓ WAR copiado exitosamente"
echo ""

echo "========================================"
echo " Iniciando Tomcat..."
echo "========================================"
echo ""

# Configurar variables de entorno necesarias para Tomcat
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk"
export CATALINA_HOME="$TOMCAT_DIR"

echo "Configurando JAVA_HOME=$JAVA_HOME"
echo "Configurando CATALINA_HOME=$CATALINA_HOME"
echo ""

# Intentar iniciar Tomcat
"$TOMCAT_DIR/bin/startup.sh"

# Verificar si Tomcat se ha iniciado correctamente
if [ $? -ne 0 ]; then
    echo "❌ Error al iniciar Tomcat. Revisa los logs de Tomcat para más detalles."
    exit 1
fi

echo ""
echo "========================================"
echo " ✅ Despliegue completado"
echo "========================================"
echo ""
echo "Espera unos segundos para que Tomcat despliegue la aplicación"
echo ""
echo "Luego accede a:"
echo "   - http://localhost:8080/web/         (Inicio de EventosUy)"
echo "   - http://localhost:8080/web/inicio   (Servlet de inicio)"
echo "   - http://localhost:8080/web/test-soap (Test SOAP)"
echo ""
