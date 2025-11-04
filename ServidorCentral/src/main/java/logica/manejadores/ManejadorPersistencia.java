package logica.manejadores;

import jakarta.persistence.*;
import logica.EdicionArchivada;
import java.util.List;

/**
 * Manejador de persistencia para ediciones archivadas usando JPA con EclipseLink y HSQLDB.
 */
public class ManejadorPersistencia {
    
    private static ManejadorPersistencia instancia = null;
    private EntityManagerFactory emf;
    
    private ManejadorPersistencia() {
        try {
            // Crear el EntityManagerFactory usando la unidad de persistencia definida en persistence.xml
            emf = Persistence.createEntityManagerFactory("EdicionesArchivadas");
        } catch (Exception e) {
            System.err.println("Error al inicializar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static ManejadorPersistencia getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPersistencia();
        }
        return instancia;
    }
    
    /**
     * Persiste una edición archivada en la base de datos.
     */
    public void persistirEdicionArchivada(EdicionArchivada edicionArchivada) throws PersistenceException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        
        try {
            tx = em.getTransaction();
            tx.begin();
            
            // Verificar si ya existe
            EdicionArchivada existente = em.find(EdicionArchivada.class, edicionArchivada.getNombreEdicion());
            if (existente != null) {
                throw new PersistenceException("La edición '" + edicionArchivada.getNombreEdicion() + "' ya está archivada.");
            }
            
            em.persist(edicionArchivada);
            tx.commit();
            
            System.out.println("✓ Edición archivada persistida: " + edicionArchivada.getNombreEdicion());
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Error al persistir edición archivada: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene una edición archivada por su nombre.
     */
    public EdicionArchivada obtenerEdicionArchivada(String nombreEdicion) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(EdicionArchivada.class, nombreEdicion);
        } finally {
            em.close();
        }
    }
    
    /**
     * Lista todas las ediciones archivadas.
     */
    public List<EdicionArchivada> listarEdicionesArchivadas() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EdicionArchivada> query = em.createQuery(
                "SELECT e FROM EdicionArchivada e ORDER BY e.fechaArchivadoAnio DESC, e.fechaArchivadoMes DESC, e.fechaArchivadoDia DESC",
                EdicionArchivada.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lista ediciones archivadas de un organizador específico.
     */
    public List<EdicionArchivada> listarEdicionesArchivadasPorOrganizador(String nicknameOrganizador) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EdicionArchivada> query = em.createQuery(
                "SELECT e FROM EdicionArchivada e WHERE e.organizador = :organizador ORDER BY e.fechaArchivadoAnio DESC, e.fechaArchivadoMes DESC, e.fechaArchivadoDia DESC",
                EdicionArchivada.class
            );
            query.setParameter("organizador", nicknameOrganizador);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica si una edición está archivada.
     */
    public boolean estaArchivada(String nombreEdicion) {
        return obtenerEdicionArchivada(nombreEdicion) != null;
    }
    
    /**
     * Cierra el EntityManagerFactory al finalizar la aplicación.
     */
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✓ EntityManagerFactory cerrado.");
        }
    }
}
