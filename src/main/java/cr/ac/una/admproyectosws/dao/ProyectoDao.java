package cr.ac.una.admproyectosws.dao;

import cr.ac.una.admproyectosws.model.Proyecto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Stateless
public class ProyectoDao {
    
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    public Proyecto crear(Proyecto proyecto) {
        em.persist(proyecto);
        em.flush();
        return proyecto;
    }

    public Proyecto actualizar(Proyecto proyecto) {
        return em.merge(proyecto);
    }

    public void eliminar(Long id) {
        Proyecto proyecto = em.find(Proyecto.class, id);
        if (proyecto != null) {
            em.remove(proyecto);
        }
    }

    public Optional<Proyecto> buscarPorId(Long id) {
        try {
            Proyecto proyecto = em.find(Proyecto.class, id);
            return Optional.ofNullable(proyecto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Proyecto> obtenerTodos() {
        return em.createNamedQuery("Proyecto.findAll", Proyecto.class)
                .getResultList();
    }

    public List<Proyecto> buscarPorEstado(String estado) {
        return em.createNamedQuery("Proyecto.findByEstado", Proyecto.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<Proyecto> buscarActivos() {
        return em.createNamedQuery("Proyecto.findActivos", Proyecto.class)
                .getResultList();
    }

    // Implementación con Stream usando la Query de JPA
    public Stream<Proyecto> buscarConStreams(String filtro) {
        TypedQuery<Proyecto> query = em.createNamedQuery("Proyecto.buscar", Proyecto.class);
        query.setParameter("filtro", "%" + filtro + "%");
        return query.getResultStream();
    }

    public List<Proyecto> buscar(String filtro) {
        return buscarConStreams(filtro).toList();
    }

    public List<Proyecto> buscarPorCreador(Long creadorId) {
        String jpql = "SELECT p FROM Proyecto p WHERE p.creadoPor.id = :creadorId";
        return em.createQuery(jpql, Proyecto.class)
                .setParameter("creadorId", creadorId)
                .getResultList();
    }

    public List<Proyecto> buscarPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        String jpql = "SELECT p FROM Proyecto p WHERE " +
                     "p.fechaInicioPlanificada BETWEEN :fechaInicio AND :fechaFin";
        return em.createQuery(jpql, Proyecto.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    public Long contarPorEstado(String estado) {
        String jpql = "SELECT COUNT(p) FROM Proyecto p WHERE p.estado = :estado";
        return em.createQuery(jpql, Long.class)
                .setParameter("estado", estado)
                .getSingleResult();
    }

    public Double promedioAvance() {
        String jpql = "SELECT AVG(p.porcentajeAvance) FROM Proyecto p WHERE p.estado IN ('EN_CURSO', 'FINALIZADO')";
        return em.createQuery(jpql, Double.class)
                .getSingleResult();
    }

    public List<Proyecto> proyectosAtrasados() {
        String jpql = "SELECT p FROM Proyecto p WHERE p.fechaFinalPlanificada < CURRENT_DATE AND p.estado IN ('PLANIFICADO', 'EN_CURSO')";
        return em.createQuery(jpql, Proyecto.class)
                .getResultList();
    }
}
