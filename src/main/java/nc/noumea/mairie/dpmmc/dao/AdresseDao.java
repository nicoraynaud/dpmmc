package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AdresseDao extends GenericDao {

    public List<Secteur> getSecteursOfVille(Ville ville) {

        TypedQuery<Secteur> secteursQuery = em.createNamedQuery("getAllSecteursOfVille", Secteur.class);
        secteursQuery.setParameter("idVille", ville.getId());

        return secteursQuery.getResultList();
    }

    public List<Quartier> getQuartiersOfSecteur(Secteur secteur) {

        TypedQuery<Quartier> secteursQuery = em.createNamedQuery("getAllQuartiersOfSecteur", Quartier.class);
        secteursQuery.setParameter("idSecteur", secteur.getId());

        return secteursQuery.getResultList();
    }

    public List<NomVoie> getNomVoiesOfQuartier(Quartier selectedQuartier) {
        TypedQuery<NomVoie> nomVoiesQuery = em.createNamedQuery("getAllNomVoiesOfQuartier", NomVoie.class);
        nomVoiesQuery.setParameter("idQuartier", selectedQuartier.getId());

        return nomVoiesQuery.getResultList();
    }

    public List<String> getNumerosOfNomVoie(NomVoie selectedNomVoie) {
        Query numeroQuery = em.createNamedQuery("getAllNumerosOfNomVoie");
        numeroQuery.setParameter("idNomVoie", selectedNomVoie.getId());

        return numeroQuery.getResultList();
    }

    public List<String> getNumerosOfNomVoie(NomVoie selectedNomVoie, Quartier quartier) {
        Query numeroQuery = em.createNamedQuery("getAllNumerosOfNomVoieAndQuartier");
        numeroQuery.setParameter("idNomVoie", selectedNomVoie.getId());
        numeroQuery.setParameter("idQuarter", quartier.getId());

        return numeroQuery.getResultList();
    }

    public Adresse getAdresseFromParams(NomVoie nomVoie, String numero, Quartier quartier) {
        TypedQuery<Adresse> adresseTypedQuery = em.createNamedQuery("getAdresseFromParams", Adresse.class);
        adresseTypedQuery.setParameter("idNomVoie", nomVoie.getId());
        adresseTypedQuery.setParameter("numero", numero);
        adresseTypedQuery.setParameter("idQuartier", quartier.getId());

        List<Adresse> results = adresseTypedQuery.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    public List<Quartier> getAllQuartiersOfNomVoieAndNumero(NomVoie selectedNomVoie, String numero) {
        TypedQuery<Quartier> quartierTypedQuery = em.createNamedQuery("getAllQuartiersOfNomVoieAndNumero", Quartier.class);
        quartierTypedQuery.setParameter("idNomVoie", selectedNomVoie.getId());
        quartierTypedQuery.setParameter("numero", StringUtils.isEmpty(numero) ? "%" : numero);

        return quartierTypedQuery.getResultList();
    }

    public Adresse getAdresseFromLieuPredefini(LieuPredefini lieuPredefini) {
        TypedQuery<Adresse> quartierTypedQuery = em.createNamedQuery("getAdresseOfLieuPredefini", Adresse.class);
        quartierTypedQuery.setParameter("idLieu", lieuPredefini.getId());

        return quartierTypedQuery.getSingleResult();
    }

    public List<String> getAllAdressesDonneeBrute() {
        List<String> listeResultats = em.createQuery("select a.donneeBrute from Adresse a").getResultList();
        return listeResultats;
    }

    public Ville getVilleFromLabel(String label) {
        TypedQuery<Ville> villeQ = em.createNamedQuery("getVilleFromLabel", Ville.class);
        villeQ.setParameter("label", label);
        villeQ.setMaxResults(1);
        List<Ville> villeList = villeQ.getResultList();

        return villeList.isEmpty() ? null : villeList.get(0);
    }

    public Secteur getSecteurFromLabel(String label) {
        TypedQuery<Secteur> secteurQ = em.createNamedQuery("getSecteurFromLabel", Secteur.class);
        secteurQ.setParameter("label", label);
        secteurQ.setMaxResults(1);
        List<Secteur> secteurList = secteurQ.getResultList();

        return secteurList.isEmpty() ? null : secteurList.get(0);
    }
}
