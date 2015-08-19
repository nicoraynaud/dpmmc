package nc.noumea.mairie.dpmmc.services.impl;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.dao.FicheDao;
import nc.noumea.mairie.dpmmc.dao.ReferenceDao;
import nc.noumea.mairie.dpmmc.dao.UtilisateurDao;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.BusinessException;
import nc.noumea.mairie.dpmmc.services.interfaces.IAuthHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheReportHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import nc.noumea.mairie.dpmmc.viewModel.FicheModel;
import nc.noumea.mairie.dpmmc.viewModel.FicheSearchResultModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class FicheService implements IFicheService {

    @Autowired
    private FicheDao ficheDao;

    @Autowired
    private IAuthHelper authHelper;

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private IFicheReportHelper ficheReportHelper;

    @Override
    public List<FicheSearchResultModel> getLastFichesCloturees() {
        return getFicheByStatusAndRecidive(StatutEnum.CLOTUREE, false);
    }

    @Override
    public List<FicheSearchResultModel> getLastActivitiesWithAlerte() {
        return getFicheByStatusAndRecidive(StatutEnum.NON_CLOTUREE, true);
    }

    @Transactional
    protected List<FicheSearchResultModel> getFicheByStatusAndRecidive(StatutEnum statut, boolean returnRecidive) {
        List<Brigade> brigades = null;

        if (!authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES)) {
            brigades = utilisateurDao.getUserBrigades(authHelper.getCurrentUserIdentifiant(), new Date());
        }

        List<Historique> histos = ficheDao.getLastFichesHistoriqueFromStatutBy(statut, brigades);

        String currentUserId = authHelper.getCurrentUserIdentifiant();
        boolean hasNBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES);
        boolean hasBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_1_BRIGADE);
        List<Brigade> currentUserBrigades = utilisateurDao.getUserBrigades(authHelper.getCurrentUserIdentifiant(), new Date());

        List<FicheSearchResultModel> fichesModel = new ArrayList<>();
        for (Historique h : histos) {
            FicheSearchResultModel fm = new FicheSearchResultModel(h.getFiche());
            if (returnRecidive)
                fm.setRecidive(ficheDao.isFicheRecidive(h.getFiche()));

            fm.setCanEdit(canEdit(h.getFiche(), h, currentUserId, hasNBrigadeRole, hasBrigadeRole, currentUserBrigades));

            fichesModel.add(fm);
        }

        return fichesModel;
    }

    @Override
    @Transactional
    public List<FicheSearchResultModel> search(String numeroFiche, Long agentId, Long brigadeId, Long natureId, Long categorieId, Long secteurId, Long quartierId, Long lieuPredefiniId, Long categoriePersonneId, String nomPersonne, Date dateSaisieStart, Date dateSaisieEnd) {

        List<FicheSearchResultModel> results = new ArrayList<FicheSearchResultModel>();

        List<Historique> queryResults = ficheDao.searchFiches(numeroFiche, agentId, brigadeId, natureId, categorieId, secteurId, quartierId, lieuPredefiniId, categoriePersonneId, nomPersonne, dateSaisieStart, dateSaisieEnd);

        String currentUserId = authHelper.getCurrentUserIdentifiant();
        boolean hasNBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES);
        boolean hasBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_1_BRIGADE);
        List<Brigade> currentUserBrigades = utilisateurDao.getUserBrigades(authHelper.getCurrentUserIdentifiant(), new Date());

        for (Historique histo : queryResults) {
            FicheSearchResultModel ficheModel = new FicheSearchResultModel(histo);
            results.add(ficheModel);

            ficheModel.setCanEdit(canEdit(histo.getFiche(), histo, currentUserId, hasNBrigadeRole, hasBrigadeRole, currentUserBrigades));
        }

        return results;
    }

    @Override
    @Transactional
    public void saveFiche(Fiche fiche) {

        Historique h = new Historique();
        h.setFiche(fiche);
        fiche.getHistoriques().add(h);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());

        if (StringUtils.isEmpty(fiche.getReference())) {
            fiche.setReference(generateReference());
            fiche.setStatut(StatutEnum.NON_CLOTUREE);
            h.setAction(ActionEnum.CREATION);
        } else {
            h.setAction(ActionEnum.MODIFICATION);
        }

        ficheDao.save(fiche);
    }

    @Override
    public FicheModel getFiche(Long idFiche) {
        Fiche f = ficheDao.getFicheByIdAndFetchData(idFiche);

        List<Brigade> brigades = null;
        if (!authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES)) {
            brigades = utilisateurDao.getUserBrigades(authHelper.getCurrentUserIdentifiant(), new Date());
        }

        String currentUserId = authHelper.getCurrentUserIdentifiant();
        boolean hasNBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES);
        boolean hasBrigadeRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_MODIF_FICHE_1_BRIGADE);
        boolean hasClotureRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_CLOTURE_FICHE);
        boolean hasDeclotureRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_DECLOTURE_FICHE);
        boolean hasDeleteRole = authHelper.hasCurrentUserRole(RolesEnum.ROLE_SUPPR_FICHE);
        List<Brigade> currentUserBrigades = utilisateurDao.getUserBrigades(authHelper.getCurrentUserIdentifiant(), new Date());

        FicheModel fm = new FicheModel(f);
        fm.setCanEdit(canEdit(f, f.getHistoriques().iterator().next(), currentUserId, hasNBrigadeRole, hasBrigadeRole, currentUserBrigades));
        fm.setCanCloture(hasClotureRole && f.getStatut() == StatutEnum.NON_CLOTUREE);
        fm.setCanDecloture(hasDeclotureRole && f.getStatut() == StatutEnum.CLOTUREE);
        fm.setCanDelete(hasDeleteRole && f.getStatut() != StatutEnum.SUPPRIMEE);
        Historique creation = f.getHistoriques().iterator().next();
        fm.setCreateDate(creation.getDateAction());
        fm.setCreator(String.format("%s %s", creation.getUtilisateur().getAgent().getNom(), creation.getUtilisateur().getAgent().getPrenom()));

        Iterator<Historique> i = f.getHistoriques().iterator();
        Historique modification = null;
        while (i.hasNext()) { modification = i.next(); }
        fm.setModifiedDate(modification != null ? modification.getDateAction() : null);
        fm.setModifier(modification != null ? String.format("%s %s", modification.getUtilisateur().getAgent().getNom(), modification.getUtilisateur().getAgent().getPrenom()) : null);
        fm.setMotif(modification != null ? modification.getMotifAction() : null);

        return fm;
    }

    @Override
    @Transactional
    public Long createFicheFromBlank() {

        // Create new Fiche
        Fiche f = new Fiche();
        f.setGdhAppel(new DateTime().toDate());
        f.setReference(generateReference());
        f.setStatut(StatutEnum.NON_CLOTUREE);
        f.setCategorieSaisine(referenceDao.getDefault(CategorieSaisine.class));
        f.setContactInterpolice(referenceDao.getDefault(ContactInterpolice.class));

        // Create Historique for creation !
        Historique h = new Historique();
        h.setAction(ActionEnum.CREATION);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());
        f.addHistorique(h);

        ficheDao.save(f);
        Long id = ficheDao.getIdofFicheWithReference(f.getReference());

        return id;

    }

    @Override
    @Transactional
    public Long createFicheFromGeolocalisationVP(Long idGelocalisationVP) {

        GeolocalisationVP geolocalisationVP = ficheDao.getGeolocalisationVPWithLock(idGelocalisationVP);

        if (idGelocalisationVP == null) {
            throw new BusinessException(String.format("Aucun ticket de géolocalisation existant pour l'id donné en paramètre : [%n]", idGelocalisationVP));
        }

        if (!geolocalisationVP.isAffichee()) {
            throw new BusinessException(String.format("Une Main Courante a déjà été créée avec ce ticket de géolocalisation", idGelocalisationVP));
        }

        // Update its "afficheeé" var
        geolocalisationVP.setAffichee(false);

        // Create new Fiche
        Fiche f = new Fiche();
        f.setAdresse(geolocalisationVP.getAdresse());
        f.setGdhAppel(geolocalisationVP.getDateHeure());
        f.setReference(generateReference());
        f.setStatut(StatutEnum.NON_CLOTUREE);
        f.setCategorieSaisine(referenceDao.getDefault(CategorieSaisine.class));
        f.setContactInterpolice(referenceDao.getDefault(ContactInterpolice.class));

        // Create Historique for creation !
        Historique h = new Historique();
        h.setAction(ActionEnum.CREATION);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());
        f.addHistorique(h);

        // Create Patrouille from agents, indicatif radio and vehicule
        Patrouille p = new Patrouille();
        p.setIndicatif(geolocalisationVP.getIndicatifRadio());
        p.setVehicule(geolocalisationVP.getVehicule());
        for (Agent a : geolocalisationVP.getAgents()) {
            p.getAgents().add(a);
        }
        f.addPatrouille(p);

        ficheDao.save(f);
        Long id = ficheDao.getIdofFicheWithReference(f.getReference());

        return id;
    }

    @Override
    @Transactional
    public void clotureFiche(Long idFiche) {

        Fiche f = ficheDao.get(Fiche.class, idFiche);
        f.setStatut(StatutEnum.CLOTUREE);

        // Create Historique
        Historique h = new Historique();
        h.setAction(ActionEnum.CLOTURE);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());
        f.addHistorique(h);

    }

    @Override
    @Transactional
    public void declotureFiche(Long idFiche) {

        Fiche f = ficheDao.get(Fiche.class, idFiche);
        f.setStatut(StatutEnum.NON_CLOTUREE);

        // Create Historique
        Historique h = new Historique();
        h.setAction(ActionEnum.DECLOTURE);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());
        f.addHistorique(h);

    }

    @Override
    @Transactional
    public void supprimeFiche(Long idFiche, String motif) {

        Fiche f = ficheDao.get(Fiche.class, idFiche);
        f.setStatut(StatutEnum.SUPPRIMEE);

        // Create Historique
        Historique h = new Historique();
        h.setAction(ActionEnum.SUPPRESSION);
        h.setDateAction(new DateTime().toDate());
        h.setUtilisateur(authHelper.getCurrentUser());
        h.setMotifAction(motif);
        f.addHistorique(h);

    }

    @Override
    public byte[] getFicheReport(Long idFiche) throws DocumentException {
        Fiche f = ficheDao.getFicheByIdAndFetchData(idFiche);
        return ficheReportHelper.buildReportFromFiche(f);
    }

    protected String generateReference() {
        String reference = String.format("%d-%05d", new DateTime().getYear(), ficheDao.getNextFicheReference());
        return reference;
    }

    protected boolean canEdit(Fiche fiche, Historique firstHistorique, String currentUserId, boolean hasNBrigadeRole, boolean hasBrigadeRole, List<Brigade> currentUserBrigades) {

        if (fiche.getStatut() == StatutEnum.NON_CLOTUREE
                && (firstHistorique.getUtilisateur().getIdentifiant().equals(currentUserId)
                || hasNBrigadeRole
                || (hasBrigadeRole && doBrigadesMatch(currentUserBrigades, fiche)))) {
            return true;
        }

        return false;
    }

    protected boolean doBrigadesMatch(List<Brigade> currentUserBrigades, Fiche fiche) {

        for (Patrouille p : fiche.getPatrouilles()) {
            if (currentUserBrigades.contains(p.getIndicatif().getBrigade())) {
                return true;
            }
        }
        return false;
    }

}
