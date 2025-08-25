package cr.ac.una.admproyectosws.dao;

import cr.ac.una.admproyectosws.model.Administrador;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class AdministradorDao {
    
    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    public Administrador crear(Administrador administrador) {
        em.persist(administrador);
        em.flush();
        return administrador;
    }

    public Administrador actualizar(Administrador administrador) {
        return em.merge(administrador);
    }

    public void eliminar(Long id) {
        Administrador admin = em.find(Administrador.class, id);
        if (admin != null) {
            em.remove(admin);
        }
    }

    public Optional<Administrador> buscarPorId(Long id) {
        try {
            Administrador admin = em.find(Administrador.class, id);
            return Optional.ofNullable(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Administrador> buscarPorUsuario(String usuario) {
        try {
            Administrador admin = em.createNamedQuery("Administrador.findByUsuario", Administrador.class)
                    .setParameter("usuario", usuario)
                    .getSingleResult();
            return Optional.of(admin);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Administrador> login(String usuario, String password) {
        try {
            Administrador admin = em.createNamedQuery("Administrador.login", Administrador.class)
                    .setParameter("usuario", usuario)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.of(admin);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Administrador> obtenerTodos() {
        return em.createNamedQuery("Administrador.findAll", Administrador.class)
                .getResultList();
    }

    public List<Administrador> buscarPorEstado(String estado) {
        return em.createNamedQuery("Administrador.findByEstado", Administrador.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    public List<Administrador> buscar(String filtro) {
        String sql = "SELECT a FROM Administrador a WHERE " +
                     "LOWER(a.nombre) LIKE LOWER(:filtro) OR " +
                     "LOWER(a.apellidos) LIKE LOWER(:filtro) OR " +
                     "LOWER(a.usuario) LIKE LOWER(:filtro) OR " +
                     "LOWER(a.correo) LIKE LOWER(:filtro)";
        
        return em.createQuery(sql, Administrador.class)
                .setParameter("filtro", "%" + filtro + "%")
                .getResultList();
    }

    public boolean existeUsuario(String usuario) {
        Query query = em.createQuery("SELECT COUNT(a) FROM Administrador a WHERE a.usuario = :usuario");
        query.setParameter("usuario", usuario);
        return ((Long) query.getSingleResult()) > 0;
    }

    public boolean existeCorreo(String correo) {
        Query query = em.createQuery("SELECT COUNT(a) FROM Administrador a WHERE a.correo = :correo");
        query.setParameter("correo", correo);
        return ((Long) query.getSingleResult()) > 0;
    }

    public boolean existeCedula(String cedula) {
        Query query = em.createQuery("SELECT COUNT(a) FROM Administrador a WHERE a.cedula = :cedula");
        query.setParameter("cedula", cedula);
        return ((Long) query.getSingleResult()) > 0;
    }
}
