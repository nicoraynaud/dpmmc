package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.CategorieSaisine;
import nc.noumea.mairie.dpmmc.domain.IndicatifRadio;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ReferenceDao extends GenericDao {

    public List<IndicatifRadio> getIndicatifRadiosFromBrigade(Brigade brigade) {
        TypedQuery<IndicatifRadio> query = em.createNamedQuery("getIndicatifRadiosFromBrigade", IndicatifRadio.class);
        query.setParameter("idBrigade", brigade.getId());
        return query.getResultList();
    }

    public <T> T getDefault(Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);

        Predicate p = cb.isTrue(rootEntry.<Boolean>get("parDefaut"));

        cq.where(p);
        TypedQuery<T> allQuery = em.createQuery(cq.select(rootEntry));
        allQuery.setMaxResults(1);
        List<T> results = allQuery.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }
}
