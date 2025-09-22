package cr.ac.una.admproyectosws.dao;

import cr.ac.una.admproyectosws.model.Actividad;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class ActividadDao {
    
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

     // Esto crea una nueva actividad
    public Actividad crear(Actividad actividad) {
        em.persist(actividad);
        em.flush();
        return actividad;
    }

    // Esto actualiza una actividad existente
    public Actividad actualizar(Actividad actividad) {
        return em.merge(actividad);
    }

    // Esto elimina la actividad si existe
    public void eliminar(Long id) {
        Actividad actividad = em.find(Actividad.class, id);
        if (actividad != null) {
            em.remove(actividad);
        }
    }

    // Esto busca una actividad por su id
    public Optional<Actividad> buscarPorId(Long id) {
        try {
            Actividad actividad = em.find(Actividad.class, id);
            return Optional.ofNullable(actividad);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Esto trae todas las actividades de un proyecto
    public List<Actividad> buscarPorProyecto(Long proyectoId) {
        return em.createNamedQuery("Actividad.findByProyecto", Actividad.class)
                .setParameter("proyectoId", proyectoId)
                .getResultList();
    }

    // Esto trae las actividades que están en un estado especifico
    public List<Actividad> buscarPorEstado(String estado) {
        return em.createNamedQuery("Actividad.findByEstado", Actividad.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    // Esto trae las actividades de un proyecto que además coinciden con un estado
    public List<Actividad> buscarPorProyectoYEstado(Long proyectoId, String estado) {
        return em.createNamedQuery("Actividad.findByProyectoAndEstado", Actividad.class)
                .setParameter("proyectoId", proyectoId)
                .setParameter("estado", estado)
                .getResultList();
    }

    // Esto cambia el número de orden de una actividad 
    public void actualizarOrden(Long actividadId, Integer nuevoOrden) {
        Actividad actividad = em.find(Actividad.class, actividadId);
        if (actividad != null) {
            actividad.setOrdenEjecucion(nuevoOrden);
            em.merge(actividad);
        }
    }

    // Esto reacomoda varias actividades según el orden 
    public void reordenarActividades(Long proyectoId, List<Long> nuevoOrden) {
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Long actividadId = nuevoOrden.get(i);
            actualizarOrden(actividadId, i + 1);
        }
    }

    // Esto calcula cual sería el siguiente orden disponible dentro de un proyecto
    public Integer obtenerSiguienteOrden(Long proyectoId) {
        String jpql = "SELECT COALESCE(MAX(a.ordenEjecucion), 0) + 1 FROM Actividad a WHERE a.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Integer.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }

    // Esto lista las actividades asignadas a un correo, ordenadas por la fecha final planificada.
    public List<Actividad> buscarPorEncargado(String encargadoCorreo) {
        String jpql = "SELECT a FROM Actividad a WHERE a.encargadoCorreo = :correo ORDER BY a.fechaFinalPlanificada";
        return em.createQuery(jpql, Actividad.class)
                .setParameter("correo", encargadoCorreo)
                .getResultList();
    }

    // Esto trae las actividades atrasadas 
    public List<Actividad> actividadesVencidas() {
        String jpql = "SELECT a FROM Actividad a WHERE a.fechaFinalPlanificada < CURRENT_DATE AND a.estado IN ('PLANIFICADA', 'EN_CURSO')";
        return em.createQuery(jpql, Actividad.class)
                .getResultList();
    }
    
 // Esto cuenta cuántas actividades hay en el estado
    public Long contarPorEstado(String estado) {
        String jpql = "SELECT COUNT(a) FROM Actividad a WHERE a.estado = :estado";
        return em.createQuery(jpql, Long.class)
                .setParameter("estado", estado)
                .getSingleResult();
    }
}
