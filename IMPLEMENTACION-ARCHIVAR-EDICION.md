# Implementación: Caso de Uso "Archivar Edición de Evento"

## Resumen
Implementación del caso de uso que permite a los organizadores archivar ediciones de eventos que ya han finalizado, con persistencia JPA usando EclipseLink y HSQLDB.

## Cambios Realizados

### 1. ServidorCentral

#### 1.1 Configuración JPA
- **Archivo**: `ServidorCentral/src/main/resources/META-INF/persistence.xml`
  - Configurado EntityManagerFactory con EclipseLink
  - Base de datos HSQLDB embebida en `./data/edicionesArchivadas`
  - Persistencia automática de esquema

#### 1.2 Nuevas Clases

**EdicionArchivada.java** (`logica/EdicionArchivada.java`)
- Entidad JPA para persistir ediciones archivadas
- Anota con `@Entity` y `@Table`
- Almacena todos los datos de la edición incluyendo fecha de archivado

**ManejadorPersistencia.java** (`logica/manejadores/ManejadorPersistencia.java`)
- Singleton que gestiona el EntityManagerFactory
- Métodos para persistir, consultar y listar ediciones archivadas
- Queries JPQL para filtrar por organizador

**Excepciones:**
- `EdicionNoFinalizadaException.java` - Cuando se intenta archivar edición no finalizada
- `EdicionYaArchivadaException.java` - Cuando la edición ya está archivada

#### 1.3 Modificaciones en Clases Existentes

**EstadoEdicion.java**
```java
public enum EstadoEdicion {
    ACEPTADA,
    INGRESADA,
    RECHAZADA,
    ARCHIVADA  // ← NUEVO
}
```

**ControladorEvento.java** - Nuevos métodos:
- `archivarEdicion(String nomEdicion)` - Archiva una edición validando que esté finalizada y sea ACEPTADA
- `listarEdicionesArchivables(String organizador)` - Lista ediciones que pueden archivarse
- `estaEdicionArchivada(String nomEdicion)` - Verifica si está archivada
- `listarEdicionesArchivadasPorOrganizador(String organizador)` - Lista archivadas del organizador
- `seleccionarEvento(...)` - **MODIFICADO** para filtrar ediciones ARCHIVADAS solo en consulta evento público

**IControladorEvento.java** - Agregadas firmas de métodos nuevos

**PublicadorControlador.java** - Métodos SOAP publicados:
```java
@WebMethod String archivarEdicion(String nombreEdicion)
@WebMethod String[] listarEdicionesArchivables(String organizador)
@WebMethod boolean estaEdicionArchivada(String nombreEdicion)
@WebMethod String[] listarEdicionesArchivadasPorOrganizador(String organizador)
```

### 2. Servidor Web

#### 2.1 Nuevo Servlet

**ArchivarEdicionServlet.java** (`servlets/ArchivarEdicionServlet.java`)
- Mapeo: `/archivarEdicion`
- Método POST
- Validaciones:
  - Usuario logueado y es organizador
  - La edición existe y pertenece al organizador
- Llama al servicio SOAP para archivar
- Redirecciona a edicionesOrganizadas con mensaje de éxito/error

#### 2.2 Modificaciones en Servlets

**EdicionesOrganizadasServlet.java**
- Pasa mensajes de éxito/error desde la sesión a la vista

**PerfilUsuarioServlet.java**
- Carga lista de ediciones archivadas además de aceptadas/ingresadas/rechazadas
- Muestra archivadas en perfiles propios y públicos

**ConsultaEdicionServlet.java**
- Ya pasa información necesaria (finalizado, organizador, estado)

#### 2.3 Modificaciones en JSPs

**edicionesOrganizadas.jsp**
- Muestra mensaje de éxito cuando se archiva correctamente
- Botón "Archivar Edición" para ediciones finalizadas y ACEPTADAS
- Indicador "(Archivada)" para ediciones archivadas
- Las ediciones archivadas SIGUEN apareciendo en la lista

**perfilUsuario.jsp**
- Nueva sección en tabla de ediciones para estado "Archivada"
- Muestra ediciones archivadas del organizador

**consultaEdicion.jsp**
- Botón "Archivar Edición" para organizadores (si la edición ya finalizó y es ACEPTADA)
- Confirmación JavaScript antes de archivar

#### 2.4 Excepciones copiadas
- `EdicionNoFinalizadaException.java`
- `EdicionYaArchivadaException.java`

## Comportamiento

### Dónde SÍ aparecen ediciones archivadas:
✓ Ediciones Organizadas (vista del organizador)
✓ Perfil de Usuario (sección Ediciones del organizador)
✓ Consulta Edición individual (acceso directo por URL)

### Dónde NO aparecen ediciones archivadas:
✗ Consulta Evento (listado de ediciones del evento)

### Validaciones para archivar:
1. El usuario debe estar logueado como organizador
2. La edición debe pertenecer al organizador
3. La edición debe estar en estado ACEPTADA
4. La edición debe haber finalizado (fecha fin < fecha actual)
5. La edición no debe estar ya archivada

## Persistencia

- Las ediciones archivadas se guardan en HSQLDB en `./data/edicionesArchivadas`
- Se persiste información completa de la edición más fecha de archivado
- El estado en memoria de la edición cambia a ARCHIVADA
- La persistencia es automática mediante JPA/EclipseLink

## Próximos pasos necesarios

### ⚠️ IMPORTANTE - Recompilar cliente SOAP
Después de agregar los nuevos métodos a `PublicadorControlador`, es necesario:

```powershell
cd servidor-web
mvn clean compile -Dwsimport=true
```

Esto regenerará las clases del cliente SOAP con los nuevos métodos:
- `archivarEdicion(String)`
- `listarEdicionesArchivables(String)`
- `estaEdicionArchivada(String)`
- `listarEdicionesArchivadasPorOrganizador(String)`

### Notas sobre JPA
- La entidad `EdicionArchivada` es necesaria porque JPA requiere clases con `@Entity`
- No se puede usar directamente `Edicion.java` porque ya tiene su propia lógica de negocio
- Esta separación permite:
  - Mantener el historial completo de ediciones archivadas
  - No mezclar lógica de persistencia con lógica de negocio
  - Consultas específicas sobre ediciones archivadas

## Testing

Para probar la funcionalidad:
1. Iniciar ServidorCentral
2. Iniciar servidor-web
3. Loguearse como organizador
4. Crear/seleccionar una edición ACEPTADA con fecha de fin pasada
5. Ir a "Ediciones Organizadas" o "Consulta Edición"
6. Click en "Archivar Edición"
7. Verificar que:
   - Aparece mensaje de éxito
   - La edición sigue en la lista con indicador "(Archivada)"
   - Ya no aparece en consulta evento
   - Aparece en perfil del organizador
