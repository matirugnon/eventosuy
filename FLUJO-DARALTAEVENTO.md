# Flujo Completo: darAltaEvento con SOAP

## 📊 Diagrama del Flujo

```
┌─────────────────────────────────────────────────────────────────────┐
│                         NAVEGADOR WEB                               │
│  Usuario completa formulario en: /altaEvento                       │
│  - Nombre del evento                                                │
│  - Sigla                                                            │
│  - Descripción                                                      │
│  - Categorías (checkbox múltiple)                                  │
└────────────────────────┬────────────────────────────────────────────┘
                         │ HTTP POST
                         │ Content-Type: application/x-www-form-urlencoded
                         ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    TOMCAT (Puerto 8080)                             │
│                    servidor-web/web.war                             │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  AltaEventoServlet.java                                      │  │
│  │  (@WebServlet("/altaEvento"))                                │  │
│  │                                                              │  │
│  │  doPost() {                                                  │  │
│  │    1. Validar sesión (usuario logueado)                     │  │
│  │    2. Verificar rol = "organizador"                         │  │
│  │    3. Obtener parámetros del request:                       │  │
│  │       - nombre = request.getParameter("nombre")             │  │
│  │       - sigla = request.getParameter("sigla")               │  │
│  │       - descripcion = request.getParameter("descripcion")   │  │
│  │       - categorias[] = request.getParameterValues(...)      │  │
│  │                                                              │  │
│  │    4. Validaciones:                                          │  │
│  │       ✓ Nombre no vacío                                     │  │
│  │       ✓ Sigla no vacía                                      │  │
│  │       ✓ Al menos una categoría seleccionada                │  │
│  │                                                              │  │
│  │    5. Crear DTFecha con fecha actual:                       │  │
│  │       LocalDate hoy = LocalDate.now()                       │  │
│  │       DTFecha fechaAlta = new soap.DTFecha()                │  │
│  │       fechaAlta.setDia(hoy.getDayOfMonth())                 │  │
│  │       fechaAlta.setMes(hoy.getMonthValue())                 │  │
│  │       fechaAlta.setAnio(hoy.getYear())                      │  │
│  │                                                              │  │
│  │    6. Convertir categorías[] a StringArray:                 │  │
│  │       StringArray categoriasArray = new StringArray()       │  │
│  │       for (String cat : categorias) {                       │  │
│  │         categoriasArray.getItem().add(cat)                  │  │
│  │       }                                                      │  │
│  │                                                              │  │
│  │    7. LLAMADA SOAP:                                         │  │
│  │       PublicadorControlador pub =                           │  │
│  │         SoapClientHelper.getPublicadorControlador()         │  │
│  │                                                              │  │
│  │       boolean resultado = pub.darAltaEvento(                │  │
│  │         nombre,           // "Conferencia Tech 2025"       │  │
│  │         descripcion,      // "Evento sobre..."             │  │
│  │         fechaAlta,        // DTFecha(24, 10, 2025)         │  │
│  │         sigla,            // "CONFTECH"                     │  │
│  │         categoriasArray   // ["Tecnología", "Innovación"]  │  │
│  │       )                                                      │  │
│  │                                                              │  │
│  │    8. Procesar respuesta:                                   │  │
│  │       if (resultado) {                                      │  │
│  │         session.setAttribute("datosMensaje", "Éxito")      │  │
│  │         redirect("/inicio")                                 │  │
│  │       } else {                                              │  │
│  │         mostrar error                                       │  │
│  │       }                                                      │  │
│  │  }                                                           │  │
│  └──────────────────────┬───────────────────────────────────────┘  │
└─────────────────────────┼───────────────────────────────────────────┘
                          │ SOAP Request (XML over HTTP)
                          │ URL: http://localhost:9128/publicador
                          │ SOAPAction: "darAltaEvento"
                          │ 
                          │ <?xml version="1.0"?>
                          │ <soap:Envelope>
                          │   <soap:Body>
                          │     <darAltaEvento>
                          │       <nombreEvento>Conferencia Tech 2025</nombreEvento>
                          │       <descripcion>Evento sobre...</descripcion>
                          │       <fechaAlta>
                          │         <dia>24</dia>
                          │         <mes>10</mes>
                          │         <anio>2025</anio>
                          │       </fechaAlta>
                          │       <sigla>CONFTECH</sigla>
                          │       <categorias>Tecnología</categorias>
                          │       <categorias>Innovación</categorias>
                          │     </darAltaEvento>
                          │   </soap:Body>
                          │ </soap:Envelope>
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│              SERVIDOR CENTRAL (Puerto 9128)                         │
│              ServidorCentral.jar                                    │
│              JAX-WS SOAP Server                                     │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  PublicadorControlador.java                                  │  │
│  │  (@WebService, @SOAPBinding)                                 │  │
│  │                                                              │  │
│  │  private final ControladorEvento ctrl = new ControladorEvento();│
│  │                                                              │  │
│  │  @WebMethod                                                  │  │
│  │  public boolean darAltaEvento(                               │  │
│  │      String nombreEvento,      // "Conferencia Tech 2025"   │  │
│  │      String descripcion,        // "Evento sobre..."         │  │
│  │      DTFecha fechaAlta,         // DTFecha(24, 10, 2025)    │  │
│  │      String sigla,              // "CONFTECH"                │  │
│  │      String[] categorias        // ["Tecnología", "Innovación"] │
│  │  ) {                                                         │  │
│  │      // 1. Convertir String[] a Set<String>:                │  │
│  │      Set<String> categoriasSet;                             │  │
│  │      if (categorias == null || categorias.length == 0) {    │  │
│  │          categoriasSet = new HashSet<>();                   │  │
│  │      } else {                                                │  │
│  │          categoriasSet = new HashSet<>(Arrays.asList(categorias));│
│  │      }                                                       │  │
│  │                                                              │  │
│  │      // 2. Delegar a la lógica de negocio:                  │  │
│  │      return ctrl.darAltaEvento(                             │  │
│  │          nombreEvento,                                      │  │
│  │          descripcion,                                       │  │
│  │          fechaAlta,                                         │  │
│  │          sigla,                                             │  │
│  │          categoriasSet                                      │  │
│  │      );                                                      │  │
│  │  }                                                           │  │
│  └──────────────────────┬───────────────────────────────────────┘  │
│                         │                                           │
│                         ▼                                           │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ControladorEvento.java (Lógica de Negocio)                 │  │
│  │  (Singleton pattern)                                         │  │
│  │                                                              │  │
│  │  private ManejadorEventos manejadorE = ManejadorEventos.getInstance();│
│  │                                                              │  │
│  │  public boolean darAltaEvento(                               │  │
│  │      String nomEvento,                                       │  │
│  │      String desc,                                            │  │
│  │      DTFecha fechaAlta,                                      │  │
│  │      String sigla,                                           │  │
│  │      Set<String> nomcategorias                              │  │
│  │  ) {                                                         │  │
│  │      try {                                                   │  │
│  │          // 1. Validar que no exista:                       │  │
│  │          if (existeEvento(nomEvento)) {                     │  │
│  │              return false; // Ya existe                     │  │
│  │          }                                                   │  │
│  │                                                              │  │
│  │          // 2. Validar categorías:                          │  │
│  │          if (nomcategorias == null || nomcategorias.isEmpty()) {│
│  │              return false; // Sin categorías                │  │
│  │          }                                                   │  │
│  │                                                              │  │
│  │          // 3. Validar fecha:                               │  │
│  │          if (!esFechaValida(fechaAlta.getDia(),            │  │
│  │                             fechaAlta.getMes(),             │  │
│  │                             fechaAlta.getAnio())) {         │  │
│  │              return false; // Fecha inválida                │  │
│  │          }                                                   │  │
│  │                                                              │  │
│  │          // 4. Obtener objetos Categoria desde sus nombres: │  │
│  │          Set<Categoria> categorias =                        │  │
│  │              manejadorE.getCategorias(nomcategorias);       │  │
│  │                                                              │  │
│  │          // 5. Crear objeto Evento:                         │  │
│  │          Evento eve = new Evento(                           │  │
│  │              nomEvento,     // "Conferencia Tech 2025"     │  │
│  │              desc,          // "Evento sobre..."           │  │
│  │              fechaAlta,     // DTFecha(24, 10, 2025)       │  │
│  │              sigla,         // "CONFTECH"                   │  │
│  │              categorias,    // Set<Categoria>              │  │
│  │              null           // imagen (opcional)            │  │
│  │          );                                                  │  │
│  │                                                              │  │
│  │          // 6. Persistir en el manejador:                   │  │
│  │          manejadorE.addEvento(eve);                         │  │
│  │                                                              │  │
│  │          return true; // ✅ Éxito                           │  │
│  │                                                              │  │
│  │      } catch (Exception e) {                                │  │
│  │          return false; // ❌ Error                          │  │
│  │      }                                                       │  │
│  │  }                                                           │  │
│  └──────────────────────┬───────────────────────────────────────┘  │
│                         │                                           │
│                         ▼                                           │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ManejadorEventos.java (Manejador de Persistencia)          │  │
│  │  (Singleton pattern)                                         │  │
│  │                                                              │  │
│  │  // Colección en memoria (HashMap):                         │  │
│  │  private Map<String, Evento> eventos = new HashMap<>();     │  │
│  │  private Map<String, Categoria> categorias = new HashMap<>();│  │
│  │  private Map<String, Edicion> ediciones = new HashMap<>();  │  │
│  │                                                              │  │
│  │  public void addEvento(Evento evento) {                     │  │
│  │      // 1. Obtener nombre del evento como clave:           │  │
│  │      String nombreEvento = evento.getNombre();              │  │
│  │                           // "Conferencia Tech 2025"        │  │
│  │                                                              │  │
│  │      // 2. Agregar al HashMap:                              │  │
│  │      eventos.put(nombreEvento, evento);                     │  │
│  │                                                              │  │
│  │      // NOTA: En un sistema real, aquí se haría:           │  │
│  │      // - INSERT INTO eventos VALUES (...)                  │  │
│  │      // - entityManager.persist(evento)                     │  │
│  │      // - Pero en este caso usa HashMap en memoria          │  │
│  │  }                                                           │  │
│  │                                                              │  │
│  │  public Evento obtenerEvento(String nombreEvento) {         │  │
│  │      return eventos.get(nombreEvento);                      │  │
│  │  }                                                           │  │
│  │                                                              │  │
│  │  public Set<Categoria> getCategorias(Set<String> nombres) { │  │
│  │      Set<Categoria> result = new HashSet<>();              │  │
│  │      for (String nom : nombres) {                           │  │
│  │          Categoria cat = categorias.get(nom);               │  │
│  │          if (cat != null) result.add(cat);                  │  │
│  │      }                                                       │  │
│  │      return result;                                         │  │
│  │  }                                                           │  │
│  └──────────────────────┬───────────────────────────────────────┘  │
└─────────────────────────┼───────────────────────────────────────────┘
                          │
                          │ Base de datos: HashMap en memoria
                          │ (En proyecto real: HSQLDB, PostgreSQL, etc.)
                          │
                          ▼
                ┌─────────────────────────┐
                │ HashMap<String, Evento> │
                │ Key: "Conferencia Tech" │
                │ Value: Evento {         │
                │   nombre: "Conf..."     │
                │   descripcion: "..."    │
                │   fechaAlta: DTFecha    │
                │   sigla: "CONFTECH"     │
                │   categorias: Set<Cat>  │
                │   ediciones: Set<Ed>    │
                │ }                       │
                └─────────────────────────┘
```

---

## 🔄 Flujo de Respuesta (Return Path)

```
ManejadorEventos.addEvento(evento)
         │
         │ return void (éxito implícito)
         ▼
ControladorEvento.darAltaEvento() 
         │
         │ return true
         ▼
PublicadorControlador.darAltaEvento()
         │
         │ return true (SOAP response)
         │
         │ <?xml version="1.0"?>
         │ <soap:Envelope>
         │   <soap:Body>
         │     <darAltaEventoResponse>
         │       <return>true</return>
         │     </darAltaEventoResponse>
         │   </soap:Body>
         │ </soap:Envelope>
         ▼
AltaEventoServlet.doPost()
         │
         │ if (resultado == true) {
         │   session.setAttribute("datosMensaje", "✅ Éxito")
         │   response.sendRedirect("/inicio")
         │ }
         ▼
Navegador recibe HTTP 302 Redirect
         │
         ▼
GET /inicio → inicioServlet → muestra lista de eventos
```

---

## 📦 Objetos y Conversiones

### 1. **En el Servlet (servidor-web):**
```java
// Tipos SOAP generados automáticamente por JAX-WS:
soap.DTFecha fechaAlta = new soap.DTFecha();
fechaAlta.setDia(24);
fechaAlta.setMes(10);
fechaAlta.setAnio(2025);

soap.StringArray categorias = new soap.StringArray();
categorias.getItem().add("Tecnología");
categorias.getItem().add("Innovación");

soap.PublicadorControlador publicador = ...;
boolean resultado = publicador.darAltaEvento(nombre, desc, fechaAlta, sigla, categorias);
```

### 2. **En el Publicador (ServidorCentral):**
```java
// JAX-WS deserializa automáticamente el XML a objetos Java:
@WebMethod
public boolean darAltaEvento(
    String nombreEvento,        // String normal
    String descripcion,         // String normal
    DTFecha fechaAlta,          // logica.datatypesyenum.DTFecha
    String sigla,               // String normal
    String[] categorias         // Array de Strings
) {
    // Convertir String[] a Set<String>:
    Set<String> categoriasSet = new HashSet<>(Arrays.asList(categorias));
    
    // Llamar a la lógica:
    return ctrl.darAltaEvento(nombreEvento, descripcion, fechaAlta, sigla, categoriasSet);
}
```

### 3. **En el Controlador (lógica):**
```java
public boolean darAltaEvento(
    String nomEvento,           // "Conferencia Tech 2025"
    String desc,                // "Evento sobre tecnología..."
    DTFecha fechaAlta,          // logica.datatypesyenum.DTFecha
    String sigla,               // "CONFTECH"
    Set<String> nomcategorias   // {"Tecnología", "Innovación"}
) {
    // Obtener objetos Categoria desde los nombres:
    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);
    
    // Crear objeto de dominio:
    Evento eve = new Evento(nomEvento, desc, fechaAlta, sigla, categorias, null);
    
    // Persistir:
    manejadorE.addEvento(eve);
    return true;
}
```

---

## 🔑 Conceptos Clave

### **1. SOAP como Puente:**
- El servlet en `servidor-web` NO tiene acceso directo a la lógica
- Toda comunicación pasa por SOAP (HTTP + XML)
- JAX-WS convierte automáticamente:
  - Objetos Java → XML (serialización)
  - XML → Objetos Java (deserialización)

### **2. Separación de Responsabilidades:**
- **Servlet:** Maneja HTTP, sesiones, validaciones web
- **PublicadorControlador:** Expone la API SOAP, adapta tipos
- **ControladorEvento:** Lógica de negocio, validaciones de dominio
- **ManejadorEventos:** Persistencia (HashMap, en proyecto real sería BD)

### **3. DTFecha - Dos Versiones:**
- `soap.DTFecha` - Generada automáticamente por JAX-WS en servidor-web
- `logica.datatypesyenum.DTFecha` - Clase original en ServidorCentral
- JAX-WS convierte entre ambas automáticamente

### **4. Singleton Pattern:**
- `ControladorEvento` es Singleton
- `ManejadorEventos` es Singleton
- Garantiza una única instancia del HashMap en memoria

---

## ⚠️ Limitaciones Actuales

1. **Persistencia en memoria (HashMap):**
   - Los datos se pierden al reiniciar ServidorCentral
   - En proyecto real: usar HSQLDB, JPA, Hibernate

2. **Sin manejo de imágenes en SOAP:**
   - Los servlets tienen código comentado para imágenes
   - SOAP no transfiere el archivo de imagen

3. **Validaciones básicas:**
   - Podrían ser más robustas (ej: validar formato de sigla)

4. **Sin transacciones:**
   - Si falla después de `addEvento()`, no hay rollback

---

## 🎯 Ventajas de esta Arquitectura

✅ **Centralización:** La lógica está SOLO en ServidorCentral
✅ **Reutilización:** La app móvil puede usar el mismo SOAP endpoint
✅ **Separación:** servidor-web es un cliente "tonto", solo presenta datos
✅ **Escalabilidad:** ServidorCentral podría moverse a otro servidor
✅ **Múltiples clientes:** Web, móvil, desktop pueden coexistir

---

Esta es la arquitectura que necesitas replicar para TODOS los demás servlets!
