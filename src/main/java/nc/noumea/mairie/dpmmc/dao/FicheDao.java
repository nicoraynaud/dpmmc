package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAppParametersService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class FicheDao extends GenericDao {

    @Autowired
    IAppParametersService appParametersService;

    public List<Historique> getLastFichesHistoriqueFromStatutBy(StatutEnum statut, List<Brigade> brigades) {

        int maxResults = appParametersService.getQueryMaxResults();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Historique> hq = cb.createQuery(Historique.class);
        Root<Historique> historiqueRoot = hq.from(Historique.class);
        Fetch<Historique, Utilisateur> utilisateurFetch = historiqueRoot.fetch("utilisateur");
        Join<Historique, Fiche> ficheJoin = historiqueRoot.join("fiche");
        SetJoin<Fiche, Patrouille> patrouilleListJoin = ficheJoin.joinSet("patrouilles", JoinType.LEFT);
        Join<Patrouille, IndicatifRadio> indicatifRadioJoin = patrouilleListJoin.join("indicatif", JoinType.LEFT);
        Join<IndicatifRadio, Brigade> brigadeJoin = indicatifRadioJoin.join("brigade", JoinType.LEFT);

        Predicate pAll = null;

        // Return only CREATION mode Historique
        pAll = cb.equal(historiqueRoot.get("action"), ActionEnum.CREATION);
        // with Fiche statut filtered
        pAll = cb.and(pAll, cb.equal(ficheJoin.get("statut"), statut));

        // and filter by brigade if provided
        if (brigades != null && brigades.size() != 0) {
            List<Long> brigadesId = new ArrayList<Long>();
            for (Brigade b : brigades) {
                brigadesId.add(b.getId());
            }
            pAll = cb.and(pAll, brigadeJoin.get("id").in(brigadesId));
        }

        hq.where(pAll);
        hq.orderBy(cb.desc(historiqueRoot.get("fiche")));

        TypedQuery<Historique> fichesQuery = em.createQuery(hq.distinct(true));
        fichesQuery.setMaxResults(maxResults);

        return fichesQuery.getResultList();
    }

    public boolean isFicheRecidive(Fiche f) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ar.id as id_alerte, count(f.id) as recidive ");
        sb.append("from fiches f ");
        sb.append("inner join faits fa on fa.id_fiche = f.id ");
        sb.append("inner join natures_fait na on na.id = fa.id_nature ");
        sb.append("inner join alertes_recidives ar on ar.id_nature = fa.id_nature ");
        sb.append("inner join adresses adr on adr.id = f.id_adresse ");
        sb.append("inner join nom_voies nv on nv.id = adr.id_nom_voie ");
        sb.append("where ");
        sb.append("f.id != :idFiche ");
        sb.append("and na.id in (select distinct na2.id from natures_fait na2 inner join faits fa2 on fa2.id_nature = na2.id inner join fiches f2 on f2.id = fa2.id_fiche where f2.id = :idFiche) ");
        sb.append("and nv.libelle = (select nv3.libelle from nom_voies nv3 inner join adresses adr3 on adr3.id_nom_voie = nv3.id inner join fiches f3 on f3.id_adresse = adr3.id where f3.id = :idFiche) ");
        sb.append("and f.gdh_appel >= date(:dateFiche) + ar.delai ");
        sb.append("group by ar.id ");
        sb.append("having count(f.id) > ar.frequence");

        Query q = em.createNativeQuery(sb.toString());
        q.setParameter("idFiche", f.getId());
        q.setParameter("dateFiche", f.getGdhAppel());

        List r = q.getResultList();

        return r.size() != 0 ? true : false;
    }

    public List<Historique> searchFiches(String numeroFiche, Long agentId, Long brigadeId, Long natureId, Long categorieId, Long secteurId, Long quartierId, Long lieuPredefiniId, Long categoriePersonneId, String nomPersonne, Date dateSaisieStart, Date dateSaisieEnd) {

        int maxResults = appParametersService.getQueryMaxResults();

        // Query the db to fetch all results using Criteria
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Historique> hq = cb.createQuery(Historique.class);
        Root<Historique> historiqueRoot = hq.from(Historique.class);
        Join<Historique, Utilisateur> utilisateurJoin = historiqueRoot.join("utilisateur");
        Join<Utilisateur, Agent> agentJoin = utilisateurJoin.join("agent");
        Join<Historique, Fiche> ficheJoin = historiqueRoot.join("fiche");
        SetJoin<Fiche, Patrouille> patrouilleListJoin = ficheJoin.joinSet("patrouilles", JoinType.LEFT);
        Join<Patrouille, IndicatifRadio> indicatifRadioJoin = patrouilleListJoin.join("indicatif", JoinType.LEFT);
        Join<IndicatifRadio, Brigade> brigadeJoin = indicatifRadioJoin.join("brigade", JoinType.LEFT);
        SetJoin<Fiche, Fait> faitListJoin = ficheJoin.joinSet("faits", JoinType.LEFT);
        Join<Fait, NatureFait> natureFaitJoin = faitListJoin.join("nature", JoinType.LEFT);
        Join<NatureFait, CategorieFait> categorieFaitJoin = natureFaitJoin.join("categorieFait", JoinType.LEFT);
        Join<Fiche, Adresse> adresseJoin = ficheJoin.join("adresse");
        Join<Adresse, Quartier> quartierJoin = adresseJoin.join("quartier");
        Join<Quartier, Secteur> secteurJoin = quartierJoin.join("secteur");
        SetJoin<Fiche, Personne> personneListJoin = ficheJoin.joinSet("personnes", JoinType.LEFT);
        Join<Personne, CategoriePersonne> categoriePersonneJoin = personneListJoin.join("categorie", JoinType.LEFT);

        Predicate pAll = null;

        // Return only CREATION mode Historique
        pAll = cb.equal(historiqueRoot.get("action"), ActionEnum.CREATION);

        if (StringUtils.isNotEmpty(numeroFiche)) {
            Predicate q1 = cb.equal(ficheJoin.get("reference"), numeroFiche);
            pAll = cb.and(pAll, q1);
        }

        if (lieuPredefiniId != null) {
            Predicate q1 = cb.equal(ficheJoin.get("lieuPredefini"), lieuPredefiniId);
            pAll = cb.and(pAll, q1);
        }

        if (agentId != null) {
            Predicate q2 = cb.equal(agentJoin.get("id"), agentId);
            pAll = cb.and(pAll, q2);
        }

        if (brigadeId != null) {
            Predicate q3 = cb.equal(brigadeJoin.get("id"), brigadeId);
            pAll = cb.and(pAll, q3);
        }

        if (categorieId != null) {
            Predicate q4 = cb.equal(categorieFaitJoin.get("id"), categorieId);
            pAll = cb.and(pAll, q4);
        }

        if (natureId != null) {
            Predicate q5 = cb.equal(natureFaitJoin.get("id"), natureId);
            pAll = cb.and(pAll, q5);
        }

        if (secteurId != null) {
            Predicate q6 = cb.equal(secteurJoin.get("id"), secteurId);
            pAll = cb.and(pAll, q6);
        }

        if (quartierId != null) {
            Predicate q7 = cb.equal(quartierJoin.get("id"), quartierId);
            pAll = cb.and(pAll, q7);
        }

        if (categoriePersonneId != null) {
            Predicate q8 = cb.equal(categoriePersonneJoin.get("id"), categoriePersonneId);
            pAll = cb.and(pAll, q8);
        }

        if (dateSaisieStart != null) {
            Predicate q9 = cb.greaterThanOrEqualTo(historiqueRoot.<Date>get("dateAction"), dateSaisieStart);
            pAll = cb.and(pAll, q9);
        }

        if (dateSaisieEnd != null) {
            Predicate q10 = cb.lessThanOrEqualTo(historiqueRoot.<Date>get("dateAction"), dateSaisieEnd);
            pAll = cb.and(pAll, q10);
        }

        if (StringUtils.isNotEmpty(nomPersonne)) {
            Predicate q15 = cb.like(personneListJoin.<String>get("nom"), "%" + nomPersonne + "%");
            pAll = cb.and(pAll, q15);
        }

        hq.where(pAll);
        hq.orderBy(cb.desc(historiqueRoot.get("fiche")));

        TypedQuery<Historique> fichesQuery = em.createQuery(hq.distinct(true));
        fichesQuery.setMaxResults(maxResults);

        return fichesQuery.getResultList();

    }

    @Transactional
    public Long getNextFicheReference() {

        Query q = em.createNativeQuery("select nextval('dpmmc_s_fiche_ref')");
        BigInteger seqNumber = (BigInteger) q.getSingleResult();

        return seqNumber.longValue();
    }

    @Transactional
    public Fiche getFicheByIdAndFetchData(Long idFiche) {

        // Query the db to fetch the result with fetch on collections
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fiche> fq = cb.createQuery(Fiche.class);
        Root<Fiche> ficheRoot = fq.from(Fiche.class);
        Fetch<Fiche, Historique> historiqueSetJoin = ficheRoot.fetch("historiques", JoinType.LEFT);
        Fetch<Historique, Utilisateur> utilisateurJoin = historiqueSetJoin.fetch("utilisateur");
        Fetch<Utilisateur, Agent> agentJoin = utilisateurJoin.fetch("agent");

        /*Fetch<Fiche, Patrouille> patrouilleSetJoin = ficheRoot.fetch("patrouilles", JoinType.LEFT);
        Fetch<Patrouille, Agent> patrouilleAgentSetJoin = patrouilleSetJoin.fetch("agents", JoinType.LEFT);

        Fetch<Fiche, Fait> faitSetJoin = ficheRoot.fetch("faits", JoinType.LEFT);
        Fetch<Fait, NatureFait> faitJoin = faitSetJoin.fetch("nature", JoinType.LEFT);

        Fetch<Fiche, Personne> personneSetJoin = ficheRoot.fetch("personnes", JoinType.LEFT);
        Fetch<Personne, CategoriePersonne> categoriePersonneJoin = personneSetJoin.fetch("categorie", JoinType.LEFT);

        Fetch<Fiche, Procedure> procedureSetJoin = ficheRoot.fetch("procedures", JoinType.LEFT);

        Fetch<Fiche, Appelant> appelantsSetJoin = ficheRoot.fetch("appelants", JoinType.LEFT);*/

        Fetch<Fiche, Adresse> adresseJoin = ficheRoot.fetch("adresse", JoinType.LEFT);
        Fetch<Adresse, Quartier> quartierJoin = adresseJoin.fetch("quartier", JoinType.LEFT);
        Fetch<Quartier, Secteur> secteurJoin = quartierJoin.fetch("secteur", JoinType.LEFT);
        Fetch<Secteur, Ville> villeJoin = secteurJoin.fetch("ville", JoinType.LEFT);

        // Return only CREATION mode Historique
        Predicate pAll = cb.equal(ficheRoot.get("id"), idFiche);

        fq.where(pAll);

        TypedQuery<Fiche> ficheTypedQuery = em.createQuery(fq);
        System.out.println("############start#############");
        System.out.println(ficheTypedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        System.out.println("############end#############");
        ficheTypedQuery.setMaxResults(1);

        List<Fiche> result = ficheTypedQuery.getResultList();

        Fiche fiche = result.isEmpty() ? null : result.get(0);
        
        //Initialize Collections to prevent LazyInizialization No Proxy Exception
        org.hibernate.Hibernate.initialize(fiche.getPatrouilles());
        for (Patrouille p : fiche.getPatrouilles()) {
            org.hibernate.Hibernate.initialize(p.getAgents());
        }
        org.hibernate.Hibernate.initialize(fiche.getFaits());
        org.hibernate.Hibernate.initialize(fiche.getPersonnes());
        org.hibernate.Hibernate.initialize(fiche.getProcedures());
        org.hibernate.Hibernate.initialize(fiche.getAppelants());
        
        return fiche;
    }

    public GeolocalisationVP getGeolocalisationVPWithLock(Long id) {
        TypedQuery<GeolocalisationVP> query = em.createNamedQuery("getGeolocVpByIdForLock", GeolocalisationVP.class);
        query.setParameter("id", id);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query.setMaxResults(1);
        List<GeolocalisationVP> result = query.getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public Long getIdofFicheWithReference(String reference) {
        Query q = em.createNativeQuery("select id from fiches where reference like :reference");
        q.setParameter("reference", reference);

        List r = q.getResultList();

        return r.isEmpty() ? null : ((BigInteger) r.get(0)).longValue();
    }
}
