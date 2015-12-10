package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.DonneeBrutGeoloc;
import nc.noumea.mairie.dpmmc.domain.GeolocalisationVP;
import nc.noumea.mairie.dpmmc.domain.MajGeoloc;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class GeolocDao extends GenericDao {

    public Map.Entry<Long, List<GeolocalisationVP>> getAwaitingGeolocs(int max) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GeolocalisationVP> vpq = cb.createQuery(GeolocalisationVP.class);
        Root<GeolocalisationVP> geolocalisationVPRoot = vpq.from(GeolocalisationVP.class);
        geolocalisationVPRoot.fetch("indicatifRadio");
        geolocalisationVPRoot.fetch("vehicule");
        geolocalisationVPRoot.fetch("adresse");
        geolocalisationVPRoot.fetch("agents");

        Predicate pAll = cb.isTrue(geolocalisationVPRoot.<Boolean>get("affichee"));
        vpq.where(pAll);

        vpq.orderBy(cb.desc(geolocalisationVPRoot.get("dateHeure")));

        TypedQuery<GeolocalisationVP> geolocQuery = em.createQuery(vpq.distinct(true))
                .setFirstResult(0)
                .setMaxResults(max);

        CriteriaQuery<Long> total = cb.createQuery(Long.class);
        Root<GeolocalisationVP> totalRoot = total.from(GeolocalisationVP.class);
        Predicate pAllTotal = cb.isTrue(totalRoot.<Boolean>get("affichee"));
        total.where(pAllTotal);

        total.select(cb.countDistinct(totalRoot));

        TypedQuery<Long> totalQuery = em.createQuery(total);


        List<GeolocalisationVP> r1 = geolocQuery.getResultList();
        Long r2 = totalQuery.getSingleResult();
        Map.Entry result = new AbstractMap.SimpleEntry<>(r2, r1);

        return result;
    }

    public List<DonneeBrutGeoloc> getDonneesBrutSinceDate(Date date) {
        TypedQuery<DonneeBrutGeoloc> q = em.createNamedQuery("getDonneesBrutesSinceDate", DonneeBrutGeoloc.class);
        q.setParameter("date", date);
        return q.getResultList();
    }

    public MajGeoloc getMajGeolocWithLock() {
        MajGeoloc toUpdate = em.find(MajGeoloc.class, 1l, LockModeType.PESSIMISTIC_READ);
        return toUpdate;
    }

    public boolean doesGeolocVPExists(String s) {
        TypedQuery<Boolean> q = em.createNamedQuery("doesGeolocAlreadyExists", Boolean.class);
        q.setParameter("donneesGeoloc", s);
        return q.getSingleResult();

    }
}
