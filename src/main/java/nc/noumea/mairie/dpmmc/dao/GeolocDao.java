package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class GeolocDao extends GenericDao {

    public List<GeolocalisationVP> getAwaitingGeolocs() {

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

        TypedQuery<GeolocalisationVP> geolocQuery = em.createQuery(vpq.distinct(true));

        return geolocQuery.getResultList();
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
