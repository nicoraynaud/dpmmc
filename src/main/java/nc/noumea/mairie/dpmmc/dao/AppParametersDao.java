package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.ParametreApplication;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class AppParametersDao {

    @PersistenceContext(unitName = "dpmmcPersistenceUnit")
    protected EntityManager em;

    @Transactional
    public int getParameter(String code) {

        Query requeteNbResultatsMaxParPage = em.createQuery("select p from ParametreApplication p where code=:code");
        requeteNbResultatsMaxParPage.setParameter("code", code);
        ParametreApplication paramNbResultatsMaxParPage = (ParametreApplication) requeteNbResultatsMaxParPage.getSingleResult();
        int value = Integer.parseInt(paramNbResultatsMaxParPage.getValeur());

        return value;
    }
}
