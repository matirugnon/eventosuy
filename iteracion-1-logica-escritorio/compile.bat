@echo off
cd /d "c:\Users\bruno\OneDrive\Desktop\tprog\iteracion1\tpgr15\2.2"

rem Limpiar directorio bin
if exist bin rmdir /s /q bin
mkdir bin

rem Compilar excepciones
javac -d bin src/main/java/excepciones/*.java

rem Compilar DataTypes básicos
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTFecha.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/EstadoEdicion.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/NivelPatrocinio.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTUsuario.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTAsistente.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTOrganizador.java

rem Compilar clases básicas de lógica
javac -cp bin -d bin src/main/java/logica/Categoria.java
javac -cp bin -d bin src/main/java/logica/Usuario.java
javac -cp bin -d bin src/main/java/logica/Asistente.java
javac -cp bin -d bin src/main/java/logica/Organizador.java
javac -cp bin -d bin src/main/java/logica/Institucion.java

rem Compilar clases de registros y tipos
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTTipoDeRegistro.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTRegistro.java
javac -cp bin -d bin src/main/java/logica/TipoDeRegistro.java
javac -cp bin -d bin src/main/java/logica/Registro.java

rem Compilar clases de eventos y ediciones
javac -cp bin -d bin src/main/java/logica/Evento.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTEvento.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTSeleccionEvento.java
javac -cp bin -d bin src/main/java/logica/Edicion.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTEdicion.java

rem Compilar patrocinios
javac -cp bin -d bin src/main/java/logica/Patrocinio.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTPatrocinio.java
javac -cp bin -d bin src/main/java/logica/DatatypesYEnum/DTInstitucion.java

rem Compilar manejadores
javac -cp bin -d bin src/main/java/logica/manejadores/*.java

rem Compilar controladores
javac -cp bin -d bin src/main/java/logica/Controladores/*.java

rem Compilar utils
javac -cp bin -d bin src/main/java/utils/*.java

rem Compilar presentación
javac -cp bin -d bin src/main/java/presentacion/*.java

rem Compilar servlets
javac -cp "bin;lib/jakarta.servlet-api-5.0.0.jar" -d bin src/main/java/servlets/*.java

echo Compilación completada!