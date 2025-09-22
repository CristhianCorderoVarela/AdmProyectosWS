package cr.ac.una.admproyectosws.dao;

import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class SeguimientoProyectoDao {
    
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    // Esto crea un seguimiento y lo guarda 
    public SeguimientoProyecto crear(SeguimientoProyecto seguimiento) {
        em.persist(seguimiento);
        em.flush();
        return seguimiento;
    }

    // Esto actualiza un seguimiento existente con los datos que envíes.
    public SeguimientoProyecto actualizar(SeguimientoProyecto seguimiento) {
        return em.merge(seguimiento);
    }

    // Esto elimina un seguimiento por su id si existe.
    public void eliminar(Long id) {
        SeguimientoProyecto seguimiento = em.find(SeguimientoProyecto.class, id);
        if (seguimiento != null) {
            em.remove(seguimiento);
        }
    }

    // Esto busca un seguimiento por id y te lo devuelve si aparece.
    public Optional<SeguimientoProyecto> buscarPorId(Long id) {
        try {
            SeguimientoProyecto seguimiento = em.find(SeguimientoProyecto.class, id);
            return Optional.ofNullable(seguimiento);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Esto trae todos los seguimientos de un proyecto, del mas reciente al mas viejo.
    public List<SeguimientoProyecto> buscarPorProyecto(Long proyectoId) {
        return em.createNamedQuery("SeguimientoProyecto.findByProyecto", SeguimientoProyecto.class)
                .setParameter("proyectoId", proyectoId)
                .getResultList();
    }

    // Esto encuentra el último seguimiento registrado para un proyecto 
    public Optional<SeguimientoProyecto> buscarUltimoPorProyecto(Long proyectoId) {
        try {
            List<SeguimientoProyecto> seguimientos = em.createNamedQuery("SeguimientoProyecto.findUltimoByProyecto", SeguimientoProyecto.class)
                    .setParameter("proyectoId", proyectoId)
                    .setMaxResults(1)
                    .getResultList();
            
            if (!seguimientos.isEmpty()) {
                return Optional.of(seguimientos.get(0));
            } else {
                return Optional.empty();
            }
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // Esto lista los seguimientos que caen entre dos fechas.
    public List<SeguimientoProyecto> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("SeguimientoProyecto.findByFecha", SeguimientoProyecto.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    // Esto trae los seguimientos creados por un administrador específico (más nuevos primero).
    public List<SeguimientoProyecto> buscarPorCreador(Long creadorId) {
        String jpql = "SELECT s FROM SeguimientoProyecto s WHERE s.creadoPor.id = :creadorId ORDER BY s.fechaSeguimiento DESC";
        return em.createQuery(jpql, SeguimientoProyecto.class)
                .setParameter("creadorId", creadorId)
                .getResultList();
    }

    // Esto devuelve los seguimientos más recientes, limitado por el número que indiques.
    public List<SeguimientoProyecto> seguimientosRecientes(int limit) {
        String jpql = "SELECT s FROM SeguimientoProyecto s ORDER BY s.fechaSeguimiento DESC";
        return em.createQuery(jpql, SeguimientoProyecto.class)
                .setMaxResults(limit)
                .getResultList();
    }

    // Esto calcula el promedio de avance reportado para un proyecto.
    public Double promedioAvancePorProyecto(Long proyectoId) {
        String jpql = "SELECT AVG(s.porcentajeAvance) FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Double.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }

    // Esto cuenta cuántos seguimientos tiene un proyecto.
    public Long contarSeguimientos(Long proyectoId) {
        String jpql = "SELECT COUNT(s) FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Long.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }
}
