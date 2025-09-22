package cr.ac.una.admproyectosws.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<T, ID> {
    
    @PersistenceContext(unitName = "ProyectoPU")
    protected EntityManager em;
    
    private final Class<T> entityClass;
    
    // Esto deja listo con que tipo de entidad va a trabajar esta clase.
    protected BaseDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    // Esto guarda una entidad nueva y la devuelve tal como quedó.
    public T crear(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }
    
    // Esto actualiza la entidad y devuelve la versión actualizada.
    public T actualizar(T entity) {
        return em.merge(entity);
    }
    
    // Esto borra la entidad por su id si existe.
    public void eliminar(ID id) {
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
    
    // Esto busca una entidad por id y te la devuelve si aparece.
    public Optional<T> buscarPorId(ID id) {
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // Esto trae todas las entidades de ese tipo.
    public List<T> obtenerTodos() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();
    }
    
    // Esto dice cuantos registros hay de ese tipo.
    public Long contar() {
        String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }
}
