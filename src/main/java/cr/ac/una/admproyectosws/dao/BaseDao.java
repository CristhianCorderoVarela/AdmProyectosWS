// ============= BASEDAO.JAVA (Clase base opcional) =============
package cr.ac.una.admproyectosws.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Clase base abstracta para los DAOs que proporciona operaciones CRUD básicas
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del ID de la entidad
 */
public abstract class BaseDao<T, ID> {
    
    @PersistenceContext(unitName = "ProyectoPU")
    protected EntityManager em;
    
    private final Class<T> entityClass;
    
    protected BaseDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public T crear(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }
    
    public T actualizar(T entity) {
        return em.merge(entity);
    }
    
    public void eliminar(ID id) {
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
    
    public Optional<T> buscarPorId(ID id) {
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public List<T> obtenerTodos() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();
    }
    
    public Long contar() {
        String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }
}