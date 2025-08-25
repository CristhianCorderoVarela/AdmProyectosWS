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

    public Actividad crear(Actividad actividad) {
        em.persist(actividad);
        em.flush();
        return actividad;
    }

    public Actividad actualizar(Actividad actividad) {
        return em.merge(actividad);
    }

    public void eliminar(Long id) {
        Actividad actividad = em.find(Actividad.class, id);
        if (actividad != null) {
            em.remove(actividad);
        }
    }

    public Optional<Actividad> buscarPorId(Long id) {
        try {
            Actividad actividad = em.find(Actividad.class, id);
            return Optional.ofNullable(actividad);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Actividad> buscarPorProyecto(Long proyectoId) {
        return em.createNamedQuery("Actividad.findByProyecto", Actividad.class)
                .setParameter("proyectoId", proyectoId)
                .getResultList();
    }

    public List<Actividad> buscarPorEstado(String estado) {
        return em.createNamedQuery("Actividad.findByEstado", Actividad.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<Actividad> buscarPorProyectoYEstado(Long proyectoId, String estado) {
        return em.createNamedQuery("Actividad.findByProyectoAndEstado", Actividad.class)
                .setParameter("proyectoId", proyectoId)
                .setParameter("estado", estado)
                .getResultList();
    }

    public void actualizarOrden(Long actividadId, Integer nuevoOrden) {
        Actividad actividad = em.find(Actividad.class, actividadId);
        if (actividad != null) {
            actividad.setOrdenEjecucion(nuevoOrden);
            em.merge(actividad);
        }
    }

    public void reordenarActividades(Long proyectoId, List<Long> nuevoOrden) {
        for (int i = 0; i < nuevoOrden.size(); i++) {
            Long actividadId = nuevoOrden.get(i);
            actualizarOrden(actividadId, i + 1);
        }
    }

    public Integer obtenerSiguienteOrden(Long proyectoId) {
        String jpql = "SELECT COALESCE(MAX(a.ordenEjecucion), 0) + 1 FROM Actividad a WHERE a.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Integer.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }

    public List<Actividad> buscarPorEncargado(String encargadoCorreo) {
        String jpql = "SELECT a FROM Actividad a WHERE a.encargadoCorreo = :correo ORDER BY a.fechaFinalPlanificada";
        return em.createQuery(jpql, Actividad.class)
                .setParameter("correo", encargadoCorreo)
                .getResultList();
    }

    public List<Actividad> actividadesVencidas() {
        String jpql = "SELECT a FROM Actividad a WHERE a.fechaFinalPlanificada < CURRENT_DATE AND a.estado IN ('PLANIFICADA', 'EN_CURSO')";
        return em.createQuery(jpql, Actividad.class)
                .getResultList();
    }

    public Long contarPorEstado(String estado) {
        String jpql = "SELECT COUNT(a) FROM Actividad a WHERE a.estado = :estado";
        return em.createQuery(jpql, Long.class)
                .setParameter("estado", estado)
                .getSingleResult();
    }
}
