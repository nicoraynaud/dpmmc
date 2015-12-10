package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.GeolocDao;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IGeolocService;
import nc.noumea.mairie.dpmmc.viewModel.GeolocFicheModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GeolocService implements IGeolocService {

    @Autowired
    private GeolocDao geolocDao;

    private Logger logger = LoggerFactory.getLogger(GeolocService.class);

    @Override
    @Transactional
    public List<GeolocFicheModel> getAwaitingGeolocs() {

        List<GeolocalisationVP> results = geolocDao.getAwaitingGeolocs();
        List<GeolocFicheModel> gfms = new ArrayList<>();

        for (GeolocalisationVP gvp : results) {
            GeolocFicheModel gfm = new GeolocFicheModel(gvp);
            gfms.add(gfm);
        }

        return gfms;
    }

    @Override
    @Transactional
    public void updateAwaitingGeolocs() {

        logger.info("Mise à jour de la liste des tickets de géoloc...");

        // Get latest update date
        MajGeoloc maj = geolocDao.getMajGeolocWithLock();
        logger.debug("La dernière mise à jour date de [{}]", maj.getDateDerniereMaj());

        // retrieve latest geoloc from view
        List<DonneeBrutGeoloc> data = geolocDao.getDonneesBrutSinceDate(maj.getDateDerniereMaj());
        logger.info("Récupération de {} données brutes (VM_GEOLOC)", data.size());

        // for each item, create a new GeolocalisationVP
        Date lastSyncedData = null;
        boolean newDateSaved = false;
        for (DonneeBrutGeoloc d : data) {
            List<String> errors = createGeolocalisationVP(d);
            if (errors != null) {
                newDateSaved = true;
                if (lastSyncedData == null || d.getDateHeure().after(lastSyncedData)) {
                    lastSyncedData = d.getDateHeure();
                }
            }
        }

        // if at least one data was created, update last update date accordingly
        if (newDateSaved) {
            maj.setDateDerniereMaj(lastSyncedData);
        }
    }

    @Override
    @Transactional
    public void removeFicheFromList(Long idGeolocalisationVP) {
        logger.info("Suppression d'un ticket de géoloc...");

        GeolocalisationVP gvp = geolocDao.get(GeolocalisationVP.class, idGeolocalisationVP);
        gvp.setAffichee(false);
        geolocDao.save(gvp);

        logger.info("Ticket de géoloc retiré de la liste.");

    }

    @Transactional(propagation = Propagation.NESTED)
    protected List<String> createGeolocalisationVP(DonneeBrutGeoloc d) {

        logger.info("Processing VM_GEOLOC : [{}]", d.toString());

        // If the record already exists, then dont process it
        if (geolocDao.doesGeolocVPExists(d.toString())) {
            logger.info("Element déjà traité. Arrêt du process.");
            return null;
        }

        logger.info("Element nouveau, début du process...");

        List<String> errors = new ArrayList<String>();
        GeolocalisationVP newGeolocalisationVP = new GeolocalisationVP();

        // Date & Heure
        newGeolocalisationVP.setDateHeure(d.getDateHeure());

        // IndicatifRadio
        List<IndicatifRadio> ir = geolocDao.getAllMatchExactActive(IndicatifRadio.class, String.class, "libelleLong", d.getLibelleIndicatifRadio());
        if (ir.size() == 0) {
            errors.add(String.format("L'indicatif %s ne correspond à aucun indicatif radio connu.", d.getLibelleIndicatifRadio()));
        } else {
            newGeolocalisationVP.setIndicatifRadio(ir.get(0));
        }

        // Vehicule
        List<Vehicule> v = geolocDao.getAllMatchExactActive(Vehicule.class, String.class, "immatriculation", d.getImmatriculationVehicule());
        if (v.size() == 0) {
            errors.add(String.format("L'immatriculation du véhicule %s ne correspond à aucun véhicule connu.", d.getImmatriculationVehicule()));
        } else {
            newGeolocalisationVP.setVehicule(v.get(0));
        }

        // Agents
        if (StringUtils.isEmpty(d.getListeMatriculesAgents())) {
            errors.add("Aucun agent n'a été associé à la patrouille de géolocalisation.");
        } else {

            for (String s : d.getListeMatriculesAgents().split(";")) {
                List<Agent> a = geolocDao.getAllMatchExact(Agent.class, Integer.class, "matricule", s);
                if (a.size() == 0) {
                    errors.add(String.format("Le matricule %s ne correspond à aucun agent connu.", s));
                } else if (a.size() > 1) {
                    errors.add(String.format("Le matricule %s correspond à plusieurs agents.", s));
                } else {
                    newGeolocalisationVP.getAgents().add(a.get(0));
                }
            }
        }

        // Adresse
        if (StringUtils.isEmpty(d.getIdAdresse())) {
            errors.add("Aucune adresse n'est défini pour la patrouille.");
        } else {
            Adresse ad = geolocDao.get(Adresse.class, Long.parseLong(d.getIdAdresse()));
            if (ad == null) {
                errors.add(String.format("L'id adresse %s ne correspond à aucune adresse connue.", d.getIdAdresse()));
            } else {
                newGeolocalisationVP.setAdresse(ad);
            }
        }
        newGeolocalisationVP.setAffichee(true);
        newGeolocalisationVP.setDonneesGeoloc(d.toString());

        // Log and store all errors
        for (String s : errors) {
            logger.warn(s);
            ErreurGeolocalisationVP error = new ErreurGeolocalisationVP();
            error.setLibelle(s);
            newGeolocalisationVP.getErreursGeocalisationVP().add(error);
            error.setGeolocalisationVP(newGeolocalisationVP);
        }

        logger.info("Sauvegarde du nouvel élément...");
        geolocDao.persist(newGeolocalisationVP);

        logger.info("Element nouveau sauvé");
        return errors;
    }

}
