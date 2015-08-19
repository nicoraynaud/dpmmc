package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class UtilisateurDao extends GenericDao {

    @Transactional
    public List<Brigade> getUserBrigades(String identifiant, Date date) {
        TypedQuery<Brigade> query = em.createNamedQuery("getBrigadesOfUtilisateur", Brigade.class);
        query.setParameter("identifiant", identifiant);
        query.setParameter("dateActuelle", date);

        return query.getResultList();
    }

    @Transactional
    public List<Utilisateur> getUtilisateurs() {
        TypedQuery<Utilisateur> query = em.createNamedQuery("getAllUtilisateurs", Utilisateur.class);

        return query.getResultList();
    }

    @Transactional
    public Utilisateur getUtilisateur(String identifiant) {
        TypedQuery<Utilisateur> query = em.createNamedQuery("getUtilisateurByIdentifiant", Utilisateur.class);
        query.setParameter("identifiant", identifiant);
        return query.getSingleResult();
    }
}
