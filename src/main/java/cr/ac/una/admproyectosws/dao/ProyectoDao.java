package cr.ac.una.admproyectosws.dao;

import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.model.Actividad; 
import jakarta.ejb.Stateless;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;   
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Stateless
public class ProyectoDao {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    // Esto crea un proyecto nuevo y lo guarda.
    public Proyecto crear(Proyecto proyecto) {
        em.persist(proyecto);
        em.flush();
        return proyecto;
    }

    // Esto actualiza un proyecto existente con los datos que envíes.
    public Proyecto actualizar(Proyecto proyecto) {
        return em.merge(proyecto);
    }

    // Esto elimina un proyecto por su id si existe.
    public void eliminar(Long id) {
        Proyecto proyecto = em.find(Proyecto.class, id);
        if (proyecto != null) {
            em.remove(proyecto);
        }
    }

    // Esto busca un proyecto por id y lo devuelve si aparece.
    public Optional<Proyecto> buscarPorId(Long id) {
        try {
            Proyecto proyecto = em.find(Proyecto.class, id);
            return Optional.ofNullable(proyecto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Esto busca el proyecto con sus actividades ya cargadas y ordenadas para que salgan en un orden lógico.
    public Optional<Proyecto> buscarPorIdRefrescadoConColecciones(Long id) {
        try {
            TypedQuery<Proyecto> q = em.createQuery(
                "SELECT DISTINCT p FROM Proyecto p " +
                "LEFT JOIN FETCH p.actividades a " +   
                "WHERE p.id = :id", Proyecto.class);

            q.setParameter("id", id);
            q.setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setHint("jakarta.persistence.cache.storeMode",    CacheStoreMode.REFRESH);

            Proyecto p = q.getSingleResult();

            em.refresh(p);

            if (p.getActividades() != null) {
                for (Actividad a : p.getActividades()) {
                    if (a != null) {
                        a.getDescripcion();
                        a.getEstado();
                    }
                }
                p.getActividades().sort((x, y) -> {
                    Integer ox = x.getOrdenEjecucion() == null ? Integer.MAX_VALUE : x.getOrdenEjecucion();
                    Integer oy = y.getOrdenEjecucion() == null ? Integer.MAX_VALUE : y.getOrdenEjecucion();
                    int cmp = ox.compareTo(oy);
                    if (cmp != 0) return cmp;
                    Date dx = x.getFechaInicioPlanificada();
                    Date dy = y.getFechaInicioPlanificada();
                    if (dx == null && dy == null) return 0;
                    if (dx == null) return 1;
                    if (dy == null) return -1;
                    return dx.compareTo(dy);
                });
            }

            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Esto trae todos los proyectos.
    public List<Proyecto> obtenerTodos() {
        return em.createNamedQuery("Proyecto.findAll", Proyecto.class).getResultList();
    }

    // Esto lista proyectos que estén en el estado que indiques.
    public List<Proyecto> buscarPorEstado(String estado) {
        return em.createNamedQuery("Proyecto.findByEstado", Proyecto.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    // Esto trae los proyectos activos 
    public List<Proyecto> buscarActivos() {
        return em.createNamedQuery("Proyecto.findActivos", Proyecto.class).getResultList();
    }

    // Esto busca proyectos por texto 
    public Stream<Proyecto> buscarConStreams(String filtro) {
        TypedQuery<Proyecto> query = em.createNamedQuery("Proyecto.buscar", Proyecto.class);
        query.setParameter("filtro", "%" + filtro + "%");
        return query.getResultStream();
    }

    // Esto busca proyectos por texto y te devuelve la lista directamente.
    public List<Proyecto> buscar(String filtro) {
        return buscarConStreams(filtro).toList();
    }

    // Esto trae los proyectos creados por un administrador específico.
    public List<Proyecto> buscarPorCreador(Long creadorId) {
        String jpql = "SELECT p FROM Proyecto p WHERE p.creadoPor.id = :creadorId";
        return em.createQuery(jpql, Proyecto.class)
                .setParameter("creadorId", creadorId)
                .getResultList();
    }

    // Esto lista proyectos que inician dentro del rango de fechas que indiques.
    public List<Proyecto> buscarPorRangoFechas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        String jpql = "SELECT p FROM Proyecto p WHERE p.fechaInicioPlanificada BETWEEN :fechaInicio AND :fechaFin";
        return em.createQuery(jpql, Proyecto.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    // Esto cuenta cuántos proyectos hay en un estado dado.
    public Long contarPorEstado(String estado) {
        String jpql = "SELECT COUNT(p) FROM Proyecto p WHERE p.estado = :estado";
        return em.createQuery(jpql, Long.class)
                .setParameter("estado", estado)
                .getSingleResult();
    }

    // Esto calcula el promedio de avance de los proyectos en curso o finalizados.
    public Double promedioAvance() {
        String jpql = "SELECT AVG(p.porcentajeAvance) FROM Proyecto p WHERE p.estado IN ('EN_CURSO', 'FINALIZADO')";
        return em.createQuery(jpql, Double.class).getSingleResult();
    }

    // Esto trae los proyectos que ya deberían haber terminado y aún no.
    public List<Proyecto> proyectosAtrasados() {
        String jpql = "SELECT p FROM Proyecto p WHERE p.fechaFinalPlanificada < CURRENT_DATE AND p.estado IN ('PLANIFICADO', 'EN_CURSO')";
        return em.createQuery(jpql, Proyecto.class).getResultList();
    }
}
