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

    public SeguimientoProyecto crear(SeguimientoProyecto seguimiento) {
        em.persist(seguimiento);
        em.flush();
        return seguimiento;
    }

    public SeguimientoProyecto actualizar(SeguimientoProyecto seguimiento) {
        return em.merge(seguimiento);
    }

    public void eliminar(Long id) {
        SeguimientoProyecto seguimiento = em.find(SeguimientoProyecto.class, id);
        if (seguimiento != null) {
            em.remove(seguimiento);
        }
    }

    public Optional<SeguimientoProyecto> buscarPorId(Long id) {
        try {
            SeguimientoProyecto seguimiento = em.find(SeguimientoProyecto.class, id);
            return Optional.ofNullable(seguimiento);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<SeguimientoProyecto> buscarPorProyecto(Long proyectoId) {
        return em.createNamedQuery("SeguimientoProyecto.findByProyecto", SeguimientoProyecto.class)
                .setParameter("proyectoId", proyectoId)
                .getResultList();
    }

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

    public List<SeguimientoProyecto> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("SeguimientoProyecto.findByFecha", SeguimientoProyecto.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    public List<SeguimientoProyecto> buscarPorCreador(Long creadorId) {
        String jpql = "SELECT s FROM SeguimientoProyecto s WHERE s.creadoPor.id = :creadorId ORDER BY s.fechaSeguimiento DESC";
        return em.createQuery(jpql, SeguimientoProyecto.class)
                .setParameter("creadorId", creadorId)
                .getResultList();
    }

    public List<SeguimientoProyecto> seguimientosRecientes(int limit) {
        String jpql = "SELECT s FROM SeguimientoProyecto s ORDER BY s.fechaSeguimiento DESC";
        return em.createQuery(jpql, SeguimientoProyecto.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public Double promedioAvancePorProyecto(Long proyectoId) {
        String jpql = "SELECT AVG(s.porcentajeAvance) FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Double.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }

    public Long contarSeguimientos(Long proyectoId) {
        String jpql = "SELECT COUNT(s) FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId";
        return em.createQuery(jpql, Long.class)
                .setParameter("proyectoId", proyectoId)
                .getSingleResult();
    }
}
