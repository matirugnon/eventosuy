#!/bin/bash

echo "===================================="
echo "Compilando Servidor Central..."
echo "===================================="

# Cambiar al directorio donde está el proyecto
cd "$(dirname "$0")/../ServidorCentral" || { echo "Error al cambiar de directorio"; exit 1; }

# Ejecutar Maven clean package
mvn clean package -DskipTests

# Verificar si Maven tuvo éxito
if [ $? -ne 0 ]; then
    echo "Error al compilar el proyecto."
    exit 1
fi

echo
echo "===================================="
echo "Iniciando GUI del Servidor Central..."
echo "===================================="

# Ejecutar la aplicación GUI
java -jar target/servidor-gui.jar &

echo "GUI iniciada. Presiona cualquier tecla para cerrar esta ventana..."
read -n 1 -s
