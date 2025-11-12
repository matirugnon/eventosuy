#!/bin/bash

echo "===================================="
echo "Compilando Servidor Central..."
echo "===================================="

cd "$(dirname "$0")/../ServidorCentral" || { echo "Error al cambiar de directorio"; exit 1; }

mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Error al compilar el proyecto."
    exit 1
fi

echo
echo "===================================="
echo "Iniciando GUI del Servidor Central..."
echo "===================================="

# Si querés desactivar publicación SOAP:
# JAVA_OPTS="-Dpublish.soap=false"
JAVA_OPTS=""

java $JAVA_OPTS -jar target/servidor-gui.jar &

echo "GUI iniciada. Servicios SOAP configurados desde Config."
echo "Presiona cualquier tecla para cerrar esta ventana..."
read -n 1 -s
