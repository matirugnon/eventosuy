# Guía rápida: inspeccionar la base de ediciones archivadas

Este proyecto usa HSQLDB en modo archivo (`jdbc:hsqldb:file`) para persistir las ediciones archivadas.
En este modo la base solo admite una conexión a la vez, por lo que el Servidor Central debe estar detenido antes de abrirla con una consola.

## Antes de abrir la base

- Detené el Servidor Central y Tomcat para liberar el lock (`edicionesArchivadas.lck`).
- Localizá el JAR de HSQLDB: se descarga vía Maven en  
  //en mi compu es aca : busquen la suya 
  `C:\Users\facu3\.m2\repository\org\hsqldb\hsqldb\2.7.2\hsqldb-2.7.2.jar`  
  COMANDO: 
java -cp "/ens/home01/f/facundo.grela/.m2/repository/org/hsqldb/hsqldb/2.7.2/hsqldb-2.7.2.jar" org.hsqldb.util.DatabaseManagerSwing --url "jdbc:hsqldb:file:/ens/devel01/tpgr15/tpgr15/ServidorCentral/data/edicionesArchivadas" --user SA

  
  
  query 
  SELECT * FROM EDICIONES_ARCHIVADAS;
  
  Como SERVIDOR: 
  
  Levanta Servidor:
java -cp "/ens/home01/f/facundo.grela/.m2/repository/org/hsqldb/hsqldb/2.7.2/hsqldb-2.7.2.jar" org.hsqldb.server.Server --database.0 file:/ens/devel01/tpgr15/tpgr15/ServidorCentral/data/edicionesArchivadas --dbname.0 Archivadas

 Levanta gui 
 java -cp "/ens/home01/f/facundo.grela/.m2/repository/org/hsqldb/hsqldb/2.7.2/hsqldb-2.7.2.jar" org.hsqldb.util.DatabaseManagerSwing --url "jdbc:hsqldb:hsql://localhost:9001/Archivadas" --user SA
 
Limpiar tabla 
DELETE FROM EDICIONES_ARCHIVADAS;


  
  
  
  (ajustá la versión si Maven trae otra).

## Abrir DatabaseManager (GUI)

1. Ejecutá en PowerShell:
   ```powershell
   java -cp "C:\Users\facu3\.m2\repository\org\hsqldb\hsqldb\2.7.2\hsqldb-2.7.2.jar" org.hsqldb.util.DatabaseManagerSwing
   ```
2. En la ventana, configurá:
   - **Type**: `HSQL Database Engine Standalone`
   // pongan su url a su repo 
   - **URL**: `jdbc:hsqldb:file:C:/Users/facu3/git/tpgr15/ServidorCentral/data/ edicionesArchivadas`
   - **User**: `SA`
   - **Password**: dejá el campo vacío.
3. Presioná **Connect** y ejecutá tus consultas (ej. `SELECT * FROM EDICIONES_ARCHIVADAS;`).
4. Cerrá el manager cuando termines y recién ahí reiniciá el Servidor Central/Tomcat.

### Versión consola

Podés usar la versión texto cambiando la clase a `org.hsqldb.util.DatabaseManager`:
```powershell
java -cp "C:\Users\facu3\.m2\repository\org\hsqldb\hsqldb\2.7.2\hsqldb-2.7.2.jar" org.hsqldb.util.DatabaseManager --url "jdbc:hsqldb:file:C:/Users/facu3/git/tpgr15/ServidorCentral/data/edicionesArchivadas" --user SA
```

## ¿Se puede inspeccionar “en vivo”?

No con la configuración actual. El modo archivo de HSQLDB bloquea los archivos para evitar corrupción, por lo que aceptar otra conexión implicaría cortar al Servidor Central.

Opciones si necesitás acceso simultáneo:

- **Levantar HSQLDB como servidor** (`java -cp hsqldb.jar org.hsqldb.server.Server ...`) y apuntar el `persistence.xml` a `jdbc:hsqldb:hsql://`. Eso ya permite varias conexiones, pero requiere cambiar la configuración y administrar el proceso del servidor HSQLDB.
- **Exponer consultas vía servicio** desde el propio Servidor Central. En lugar de abrir la base, agregá endpoints SOAP/REST que consulten la tabla archivada.

Mientras se use el modo archivo, la única forma segura de revisar los datos es detener la aplicación, abrir la base, consultar y volver a iniciarla.

## Atajos útiles

- Para evitar escribir siempre el comando, podés crear un `.bat` con el `java -cp ... DatabaseManagerSwing` y lanzarlo cuando el servidor esté detenido.
- Los archivos de la base están en `ServidorCentral/data/`. El contenido de `edicionesArchivadas.log` muestra los `CREATE TABLE` y `INSERT` generados, útil para auditorías rápidas sin abrir la consola.

## Volver la base a estado inicial

1. Detené el Servidor Central/Tomcat.
2. Eliminá (o mové a un backup) los archivos `ServidorCentral/data/edicionesArchivadas.*` y la carpeta `ServidorCentral/data/edicionesArchivadas.tmp/`.
3. Reiniciá el Servidor Central: EclipseLink recreará la tabla vacía automáticamente.

> **Tip:** Los archivos de la base ya están ignorados en Git (`ServidorCentral/data/`), así cada integrante mantiene su propio estado local sin afectar al repositorio.
