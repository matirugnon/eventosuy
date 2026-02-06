# Iteración 1 - Lógica y Estación de Trabajo

## 📋 Descripción

La **Iteración 1** corresponde al desarrollo inicial del sistema EventosUy como una **aplicación de escritorio** utilizando tecnologías Java SE y Swing. Esta iteración se enfoca en la implementación de la lógica de negocio y la interfaz gráfica desktop.

---

## 🎯 Objetivos

- Implementar el modelo de dominio completo
- Desarrollar la lógica de negocio (casos de uso)
- Crear una interfaz gráfica de usuario (GUI) con Java Swing
- Establecer la arquitectura base del sistema

---

## 🏗️ Arquitectura

### Patrón MVC Monolítico

```
┌─────────────────────────────────────────┐
│         Presentación (Swing)            │
│  - MainFrame (JFrame)                   │
│  - JInternalFrames                      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        Controladores                    │
│  - Factory                              │
│  - IControlador*                        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Manejadores (Singleton)         │
│  - ManejadorUsuario                     │
│  - ManejadorEventos                     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          Modelo de Dominio              │
│  - Usuario, Evento, Edicion, etc.       │
└─────────────────────────────────────────┘
```

---

## 📂 Estructura de Carpetas

```
iteracion-1-logica-escritorio/
├── src/                        # Código fuente
│   ├── datoprueba/            # Datos de prueba/inicialización
│   ├── excepciones/           # Excepciones personalizadas
│   ├── gui/                   # Interfaces gráficas (Swing)
│   ├── logica/                # Clases de dominio
│   ├── presentacion/          # Capa de presentación
│   │   └── Main.java          # Punto de entrada
│   └── utils/                 # Utilidades
├── lib/                       # Librerías externas
│   ├── com.jgoodies.forms_*.jar
│   └── com.jgoodies.common_*.jar
├── .settings/                 # Configuración Eclipse
├── compile.bat               # Script de compilación Windows
├── sources.txt               # Lista de archivos fuente
└── Readme                    # Documentación original
```

---

## 🗂️ Modelo de Dominio

### Clases Principales

#### Usuario (abstracta)
```java
public abstract class Usuario {
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;
    private byte[] imagen;
}
```

**Subclases:**
- `Asistente extends Usuario`
- `Organizador extends Usuario`

#### Evento
```java
public class Evento {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaCreacion;
    private String descripcion;
    private List<Edicion> ediciones;
    private List<Categoria> categorias;
}
```

#### Edicion
```java
public class Edicion {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String lugarFisico;
    private LocalDate fechaCreacion;
    private EstadoEdicion estado; // Enum
    private Evento evento;
    private Organizador organizador;
    private List<TipoDeRegistro> tiposRegistro;
    private List<Registro> registros;
    private List<Patrocinio> patrocinios;
}
```

#### Registro
```java
public class Registro {
    private LocalDate fechaRegistro;
    private double costoRegistro;
    private Asistente asistente;
    private Edicion edicion;
    private TipoDeRegistro tipoRegistro;
}
```

#### TipoDeRegistro
```java
public class TipoDeRegistro {
    private String nombre;
    private LocalDate fechaAlta;
    private String descripcion;
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia;
    private double descuento;
    private double costo;
}
```

#### Patrocinio
```java
public class Patrocinio {
    private LocalDate fecha;
    private double costo;
    private Institucion institucion;
    private Edicion edicion;
}
```

#### Institucion
```java
public class Institucion {
    private String nombre;
    private String descripcion;
    private String url;
    private List<Patrocinio> patrocinios;
}
```

#### Categoria
```java
public enum Categoria {
    DEPORTE, CHARLA, ESPECTACULO, COMIDA, INNOVACION
}
```

---

## 🎨 Interfaz Gráfica (Swing)

### Componentes Principales

#### MainFrame
- **Tipo:** JFrame (ventana principal)
- **Layout:** MDI (Multiple Document Interface)
- **Componentes:**
  - JMenuBar con menús
  - JDesktopPane para ventanas internas
  - Gestión de sesión de usuario

#### JInternalFrames (Ventanas Internas)

| Frame | Caso de Uso |
|-------|-------------|
| `AltaUsuarioFrame` | Alta de Usuario |
| `ConsultaUsuarioFrame` | Consulta de Usuario |
| `ModificarUsuarioFrame` | Modificar Datos de Usuario |
| `AltaEventoFrame` | Alta de Evento |
| `ConsultaEventoFrame` | Consulta de Evento |
| `AltaEdicionFrame` | Alta de Edición |
| `ConsultaEdicionFrame` | Consulta de Edición |
| `AltaTipoRegistroFrame` | Alta de Tipo de Registro |
| `ConsultaTipoRegistroFrame` | Consulta de Tipo de Registro |
| `RegistroAEdicionFrame` | Registro a Edición |
| `ConsultaRegistroFrame` | Consulta de Registro |
| `AltaInstitucionFrame` | Alta de Institución |
| `AltaPatrocinioFrame` | Alta de Patrocinio |
| `ConsultaPatrocinioFrame` | Consulta de Patrocinio |

### Layout Manager
- **JGoodies Forms** para layouts profesionales
- Formularios organizados con `FormLayout`

---

## 🔧 Componentes de Lógica

### Manejadores (Singleton)

#### ManejadorUsuario
```java
public class ManejadorUsuario {
    private static ManejadorUsuario instancia = null;
    private Map<String, Usuario> usuarios;
    
    public static ManejadorUsuario getInstance() {
        if (instancia == null) {
            instancia = new ManejadorUsuario();
        }
        return instancia;
    }
    
    public void agregarUsuario(Usuario usuario) throws UsuarioRepetidoException;
    public Usuario obtenerUsuario(String nickname);
    public Collection<Usuario> getUsuarios();
}
```

#### ManejadorEventos
```java
public class ManejadorEventos {
    private static ManejadorEventos instancia = null;
    private Map<String, Evento> eventos;
    
    public void agregarEvento(Evento evento) throws EventoRepetidoException;
    public Evento obtenerEvento(String nombre);
    public Collection<Evento> getEventos();
}
```

### Controladores

Implementan interfaces de casos de uso:
- `IControladorUsuario`
- `IControladorEvento`
- `IControladorEdicion`
- `IControladorRegistro`

**Factory Pattern:**
```java
public class Factory {
    private static Factory instancia;
    private IControladorUsuario iCU;
    private IControladorEvento iCE;
    // ...
    
    public static Factory getInstance() { ... }
    public IControladorUsuario getIControladorUsuario() { ... }
}
```

---

## 🎯 Casos de Uso Implementados

### ✅ Obligatorios Completos

1. **Alta de Usuario**
   - Crear Asistente o Organizador
   - Validar nickname único
   - Validar email único
   - Guardar imagen (opcional)

2. **Consulta de Usuario**
   - Listar todos los usuarios
   - Ver detalles de usuario seleccionado
   - Mostrar eventos organizados (Organizador)
   - Mostrar registros (Asistente)

3. **Alta de Evento**
   - Crear evento con categorías
   - Validar fechas
   - Asignar categorías (múltiples)

4. **Consulta de Evento**
   - Listar eventos
   - Ver detalles y ediciones
   - Ver estadísticas

5. **Alta de Edición de Evento**
   - Crear edición asociada a evento
   - Asignar organizador
   - Configurar fechas y lugar

6. **Consulta de Edición de Evento**
   - Ver detalles de edición
   - Listar tipos de registro
   - Listar patrocinios
   - Ver registros

7. **Alta de Tipo de Registro**
   - Crear tipo de registro para edición
   - Configurar vigencia
   - Definir costo y descuento

8. **Consulta de Tipo de Registro**
   - Ver detalles de tipos de registro

9. **Registro a Edición de Evento**
   - Registrar asistente a edición
   - Seleccionar tipo de registro
   - Calcular costo con descuento

### ⚠️ Opcionales/Parciales

10. **Consulta de Registro** (Implementación básica)

11. **Modificar Datos de Usuario** (Lógica implementada, GUI básica)

12. **Alta de Institución** (Lógica completa)

13. **Alta de Patrocinio** (Lógica completa)

14. **Consulta de Patrocinio** (Lógica completa)

15. **Alta de Categorías** (Sin GUI, lógica implementada)

---

## 🚀 Compilación y Ejecución

### Usando el script (Windows)

```cmd
cd iteracion-1-logica-escritorio
compile.bat
```

### Compilación manual

```bash
# Crear directorio de salida
mkdir -p bin

# Compilar
javac -d bin -cp "lib/*" @sources.txt

# O compilar todo src/:
javac -d bin -cp "lib/*" src/**/*.java
```

### Ejecución

```bash
# Windows
java -cp "bin;lib/*" presentacion.Main

# Linux/Mac
java -cp "bin:lib/*" presentacion.Main
```

---

## 📊 Datos de Prueba

La carpeta `datoprueba/` contiene clases para cargar datos iniciales:

```java
public class CargarDatos {
    public static void cargarUsuarios() { ... }
    public static void cargarEventos() { ... }
    public static void cargarEdiciones() { ... }
}
```

**Usuarios de prueba:**
- Organizadores: `eOrden`, `mGonzalez`, etc.
- Asistentes: `atorres`, `mRodriguez`, etc.

---

## ⚠️ Excepciones Personalizadas

```
excepciones/
├── UsuarioRepetidoException
├── EventoRepetidoException
├── EdicionRepetidaException
├── UsuarioNoExisteException
├── EventoNoExisteException
├── FechaInvalidaException
└── ...
```

**Ejemplo:**
```java
public class UsuarioRepetidoException extends Exception {
    public UsuarioRepetidoException(String mensaje) {
        super(mensaje);
    }
}
```

---

## 🗃️ Persistencia

**Tipo:** En memoria (volátil)

**Estructuras:**
- `HashMap<String, Usuario>` en ManejadorUsuario
- `HashMap<String, Evento>` en ManejadorEventos
- Sin base de datos
- Datos se pierden al cerrar la aplicación

---

## 🖼️ Gestión de Imágenes

- Usuario puede tener imagen de perfil
- Almacenamiento: `byte[]` en memoria
- Formatos soportados: JPG, PNG
- Visualización en GUI con `ImageIcon`

---

## 📝 Validaciones

- Nickname único
- Email único y formato válido
- Fechas consistentes (inicio < fin)
- Campos obligatorios no vacíos
- Descuentos entre 0% y 100%
- Costos no negativos

---

## 🎓 Aprendizajes y Conceptos

Esta iteración cubre:

✅ **Programación Orientada a Objetos**
- Herencia (Usuario → Asistente/Organizador)
- Polimorfismo
- Encapsulamiento
- Abstracción

✅ **Patrones de Diseño**
- MVC (Model-View-Controller)
- Singleton (Manejadores)
- Factory (Controladores)

✅ **Java Swing**
- JFrame, JDialog
- JInternalFrame (MDI)
- Layouts (FormLayout, BorderLayout)
- Eventos (ActionListener)

✅ **Colecciones Java**
- HashMap, ArrayList
- Iteradores

✅ **Manejo de Excepciones**
- Try-catch
- Excepciones personalizadas
- Propagación de excepciones

---

## 🐛 Limitaciones Conocidas

1. **Sin persistencia**: Datos se pierden al cerrar
2. **Sin concurrencia**: No soporta múltiples usuarios
3. **Validaciones básicas**: Podrían ser más robustas
4. **GUI no responsive**: Tamaños fijos
5. **Sin autenticación**: No hay login

---

## 📚 Documentación Original

Ver archivo [Readme](Readme) para el checklist original de casos de uso implementados.

---

## 🔄 Evolución a Iteración 2

Esta iteración sienta las bases para:
- Migración a arquitectura web (Iteración 2)
- Separación de capas más estricta
- Introducción de persistencia (Iteración 3)

El modelo de dominio y la lógica se reutilizan en iteraciones posteriores.

---

**Desarrollado por:** Equipo EventosUy  
**Asignatura:** Taller de Programación - Facultad de Ingeniería UdelaR
