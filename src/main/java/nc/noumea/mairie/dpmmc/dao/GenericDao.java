package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GenericDao {

    @PersistenceContext(unitName = "dpmmcPersistenceUnit")
    protected EntityManager em;

    @Transactional
    public Object save(Object obj) {
        Object o = em.merge(obj);
        em.flush();
        return o;
    }

    @Transactional
    public Object persist(Object obj) {
        em.persist(obj);
        em.flush();
        return obj;
    }

    public Object mergeOnly(Object obj) {
        return em.merge(obj);
    }

    public void persistOnly(Object obj) {
        em.persist(obj);
    }

    public boolean contains(Object obj) {
        return em.contains(obj);
    }

    public void flush() {
        em.flush();
    }

    public void clear() {
        em.clear();
    }

    @Transactional
    public void refresh(Object obj) {
        em.refresh(obj);
    }

    public <T> List<T> getAll(Class<T> c) {
        return getAll(c, null);
    }

    public <T> List<T> getAll(Class<T> c, String propOderAsc) {
        return getAll(c, propOderAsc, false);
    }

    public <T> List<T> getAllActive(Class<T> c) {
        return getAllActive(c, null);
    }

    public <T> List<T> getAllActive(Class<T> c, String propOderAsc) {
        return getAll(c, propOderAsc, true);
    }

    @Transactional(readOnly = true)
    protected <T> List<T> getAll(Class<T> c, String propOderAsc, boolean activeOnly) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(c);
        Root<T> rootEntry = cq.from(c);

        if (activeOnly) {
            cq.where(cb.isFalse(rootEntry.<Boolean>get("archivee")));
        }

        if (StringUtils.isNotEmpty(propOderAsc)) {
            cq.orderBy(cb.asc(rootEntry.get(propOderAsc)));
        }

        TypedQuery<T> allQuery = em.createQuery(cq.select(rootEntry));
        return allQuery.getResultList();
    }


    @Transactional(readOnly = true)
    public <T> T get(Class<T> c, Long id) {
        return em.find(c, id);
    }

    public <T, U> List<T> getAllMatchExact(Class<T> c, Class<U> propertyType,String propertyName, String propertyValue) {
        return getAllMatchExact(c, propertyType, propertyName, propertyValue, false);
    }

    public <T, U> List<T> getAllMatchExactActive(Class<T> c, Class<U> propertyType,String propertyName, String propertyValue) {
        return getAllMatchExact(c, propertyType, propertyName, propertyValue, true);
    }

    @Transactional(readOnly = true)
    protected <T, U> List<T> getAllMatchExact(Class<T> c, Class<U> propertyType, String propertyName, Object propertyValue, boolean activeOnly) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(c);
        Root<T> rootEntry = cq.from(c);

        Predicate p = null;

        if (propertyType == String.class) {
            p = cb.equal(cb.upper(rootEntry.<String>get(propertyName)), ((String) propertyValue).toUpperCase());
        } else {
            p = cb.equal(rootEntry.get(propertyName), propertyValue);
        }

        if (activeOnly) {
            p = cb.and(p, cb.isFalse(rootEntry.<Boolean>get("archivee")));
        }

        cq.where(p);
        TypedQuery<T> allQuery = em.createQuery(cq.select(rootEntry));
        return allQuery.getResultList();
    }

}
