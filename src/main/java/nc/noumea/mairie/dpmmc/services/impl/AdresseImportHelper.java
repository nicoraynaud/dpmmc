package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.AdresseDao;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.BusinessException;
import nc.noumea.mairie.dpmmc.services.interfaces.IAdresseImportHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.util.Pair;
import org.zkoss.util.media.Media;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class AdresseImportHelper implements IAdresseImportHelper {

    private Logger logger = LoggerFactory.getLogger(AdresseImportHelper.class);

    @Autowired
    private AdresseDao adresseDao;

    @Override
    @Transactional
    public List<String> importAddressFromFile(Media file) {

        logger.info("Importing addresses from {}...", file.getName());

        BufferedReader br = null;
        String adresse = null;
        int nbLines = 0;
        List<String> adressesFromFile = new ArrayList<>();

        logger.info("Reading content...");

        try {
            br = new BufferedReader(file.getReaderData());
            br.readLine(); // skip first line of the file (header of csv)
            while ((adresse = br.readLine()) != null) {
                nbLines++;
                adressesFromFile.add(adresse);
            }
        } catch (IOException e) {
            logger.error("An error occured while reading the file at line " + nbLines, e);
            throw new BusinessException(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("An error occured while closing the string reader of the file.", e);
                    throw new BusinessException(e);
                }
        }

        logger.info("Succesfully read {} addresses from file", adressesFromFile.size());

        // remove from list the data that already exists in database
        filterData(adressesFromFile);

        // import the remaining of the list
        Pair<List<String>, List<Adresse>> adressesToSave = importData(adressesFromFile);

        if (!adressesToSave.getX().isEmpty()) {
            return adressesToSave.getX();
        }

        // save the created addresses in db
        List<String> messages = saveAdresses(adressesToSave.getY());

        if (messages.isEmpty()) {
            logger.info("Succesfully imported {} addresses from file", adressesFromFile.size());
        }

        return messages;
    }

    protected void filterData(List<String> adressesFromFile) {

        logger.info("Filtering out addresses already in database...");

        List<String> adressesFromDatabase = adresseDao.getAllAdressesDonneeBrute();
        int nbFilteredAdr = 0;
        for (String dbAdr : adressesFromDatabase) {

            if (adressesFromFile.remove(dbAdr))
                nbFilteredAdr++;
        }

        logger.info("Filtered out {} addresses already in database.", nbFilteredAdr);
    }

    protected Pair<List<String>, List<Adresse>> importData(List<String> adressesFromFile) {

        logger.info("Creating {} addresses from file...", adressesFromFile.size());
        List<String> errorMessages = new ArrayList<>();
        List<Adresse> adressesToSave = new ArrayList<>();

        int lineNumber = 0;
        StringTokenizer st = null;
        String idAdresseFichier = null;
        String coordonneeXFichier = null;
        String coordonneeYFichier = null;
        String numeroVoieFichier = null;
        String idNomVoieFichier = null;
        String libelleNomVoieFichier = null;
        String idQuartierFichier = null;
        String libelleQuartierFichier = null;
        String libelleSecteurFichier = null;
        String libelleVilleFichier = null;

        for (String adresseString : adressesFromFile) {
            lineNumber++;

            String modifiedAddressString = adresseString;

            if (modifiedAddressString.startsWith(";")) {
                modifiedAddressString = " " + modifiedAddressString;
            }

            modifiedAddressString = modifiedAddressString.replaceAll(";;", ";-NO DATA-;");

            st = new StringTokenizer(modifiedAddressString, ";");

            Adresse adresse = null;
            NomVoie nomVoie = null;
            Quartier quartier = null;
            Secteur secteur = null;
            Ville ville = null;

            idAdresseFichier = null;
            coordonneeXFichier = null;
            coordonneeYFichier = null;
            numeroVoieFichier = null;
            idNomVoieFichier = null;
            libelleNomVoieFichier = null;
            idQuartierFichier = null;
            libelleQuartierFichier = null;
            libelleSecteurFichier = null;
            libelleVilleFichier = null;
            boolean isAdrValid = true;

            int tokenNumber = 0;
            String value = null;
            while (st.hasMoreTokens()) {
                tokenNumber++;
                value = st.nextToken();

//OBJECTID_1;OBJECTID_2;OBJECTID;APPARTEN;GESTION;IDADRS;IDTRONUM;NUM_COUR;NUM_GRAF;NUMERO;VOIE_NIM;LIBELLE;COMP_NUM;x;y;NOM_SECT;NOM;NUMERO_1
//    1           2         3       4          5    6      7        8         9         10    11      12      13     14|15|16     17    18
//12023     ;12        ;13521   ;NOUMEA ;NOUMEA;180379001500;180379001;15;15;15;180379;RUE EMILE LESSON; ;448921,9;218674,11;NORD;SEPTIEME KILOMETRE;1829

                switch (tokenNumber) {
                    case 4:
                        libelleVilleFichier = value;
                        break;
                    case 6:
                        idAdresseFichier = value;
                        break;
                    case 8:
                        numeroVoieFichier = value;
                        break;
                    case 11:
                        idNomVoieFichier = value;
                        break;
                    case 12:
                        libelleNomVoieFichier = value;
                        break;
                    case 14:
                        coordonneeXFichier = value;
                        break;
                    case 15:
                        coordonneeYFichier = value;
                        break;
                    case 16:
                        libelleSecteurFichier = value;
                        break;
                    case 17:
                        libelleQuartierFichier = value;
                        break;
                    case 18:
                        idQuartierFichier = value;
                    default:
                        break;
                }
            }

            // search if Ville already exists
            ville = adresseDao.getVilleFromLabel(libelleVilleFichier);
            if (ville == null) {
                ville = new Ville();
                ville.setLibelle(libelleVilleFichier);
            }

            secteur = adresseDao.getSecteurFromLabel(libelleSecteurFichier);
            if (secteur == null) {
                secteur = new Secteur();
                secteur.setLibelle(libelleSecteurFichier);
                secteur.setVille(ville);
            }

            try {
                Long idQuartier = Long.valueOf(idQuartierFichier);
                quartier = adresseDao.get(Quartier.class, idQuartier);
                if (quartier == null) {
                    quartier = new Quartier();
                    quartier.setId(idQuartier);
                }
            } catch (NumberFormatException e) {
                errorMessages
                        .add("Une erreur a été trouvée au niveau de l'id du quartier sur la ligne "
                                + lineNumber);
                logger.warn("An error was found for [idQuartier] on line {} ", lineNumber);
                isAdrValid = false;
            }

            quartier.setLibelle(libelleQuartierFichier);
            quartier.setSecteur(secteur);

            try {
                Long idNomVoie = Long.valueOf(idNomVoieFichier);
                nomVoie = adresseDao.get(NomVoie.class, idNomVoie);
                if (nomVoie == null) {
                    nomVoie = new NomVoie();
                    nomVoie.setId(idNomVoie);
                }
            } catch (NumberFormatException e) {
                errorMessages
                        .add("Une erreur a été trouvée au niveau de l'id de la voie sur la ligne "
                                + lineNumber);
                logger.warn("An error was found for [idVoir] on line {} ", lineNumber);
                isAdrValid = false;
            }

            nomVoie.setLibelle(libelleNomVoieFichier);

            try {
                Long idAdresse = Long.valueOf(idAdresseFichier);
                if (adresse == null) {
                    adresse = new Adresse();
                    adresse.setId(idAdresse);
                }
            } catch (NumberFormatException e) {
                errorMessages
                        .add("Une erreur a été trouvée au niveau de l'id de l'adresse sur la ligne "
                                + lineNumber);
                logger.warn("An error was found for [idAdresse] on line {} ", lineNumber);
                isAdrValid = false;
            }

            adresse.setDonneeBrute(adresseString);
            adresse.setNumeroVoie(numeroVoieFichier);

            try {
                adresse.setCoordonneeX(NumberFormat.getInstance()
                        .parse(coordonneeXFichier).floatValue());
            } catch (ParseException e) {
                errorMessages
                        .add("Une erreur a été trouvée au niveau de la coordonnée X sur la ligne "
                                + lineNumber);
                logger.warn("An error was found for [CoordonnéeX] on line {} ", lineNumber);
                isAdrValid = false;
            }

            try {
                adresse.setCoordonneeY(NumberFormat.getInstance()
                        .parse(coordonneeYFichier).floatValue());
            } catch (ParseException e) {
                errorMessages
                        .add("Une erreur a été trouvée au niveau de la coordonnée Y sur la ligne "
                                + lineNumber);
                logger.warn("An error was found for [CoordonnéeY] on line {} ", lineNumber);
                isAdrValid = false;
            }
            adresse.setNomVoie(nomVoie);
            adresse.setQuartier(quartier);

            if (isAdrValid) {
                adressesToSave.add(adresse);
            }

        }

        logger.info("Succesfully parsed {} addresses from file", adressesToSave.size());
        if (!errorMessages.isEmpty()) {
            logger.warn("Encountered {} errors during parsing. Aborting process.", errorMessages.size());
        }

        return new Pair<>(errorMessages, adressesToSave);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private List<String> saveAdresses(List<Adresse> adressesToSave) {

        logger.info("About to save {} addresses in db...", adressesToSave.size());

        List<String> errorMessages = new ArrayList<>();
        int i = 0;

        try {
            for (Adresse adr : adressesToSave) {
                Adresse realAdr = adresseDao.get(Adresse.class, adr.getId());

                // Handle case of new NomVoie
                if (adr.getNomVoie().getVersion() == null) {
                    NomVoie nv = adresseDao.get(NomVoie.class, adr.getNomVoie().getId());
                    if (nv != null) {
                        adr.setNomVoie(nv);
                    } else {
                        adresseDao.persistOnly(adr.getNomVoie());
                    }
                }
                // Handle case of new Quartier
                if (adr.getQuartier().getVersion() == null) {
                    Quartier q = adresseDao.get(Quartier.class, adr.getQuartier().getId());
                    if (q != null) {
                        adr.setQuartier(q);
                    } else {
                        adresseDao.persistOnly(adr.getQuartier());
                    }
                }

                // Then if the overall address is new, create it
                if (realAdr == null) {
                    adresseDao.persistOnly(adr);
                } else {
                    // Otherwise, just update its content
                    realAdr.setNumeroVoie(adr.getNumeroVoie());
                    realAdr.setNomVoie(adr.getNomVoie());
                    realAdr.setNumeroVoie(adr.getNumeroVoie());
                    realAdr.setCoordonneeX(adr.getCoordonneeX());
                    realAdr.setCoordonneeY(adr.getCoordonneeY());
                    realAdr.setDonneeBrute(adr.getDonneeBrute());
                    realAdr.setQuartier(adr.getQuartier());
                    adresseDao.save(realAdr);
                }
                i++;
                if (i % 2000 == 0) {
                    adresseDao.flush();
                    adresseDao.clear();
                    logger.info("Flushing adresses : " + i);
                }

            }
            adresseDao.flush();
            logger.info("Flushing adresses : " + i);

        } catch (Exception e) {
            errorMessages.add(String.format("Une erreur a été trouvée lors de l'insertion en base de données pour l'élément %d : %s", i, e));
            logger.error("Encountered error while saving addresses at number " + i , e);
        }

        if (!errorMessages.isEmpty()) {
            logger.warn("Encountered {} errors while saving addresses to databases, aborting process.", errorMessages.size());
        } else {
            logger.info("Succesfully saved {} addresses in db", adressesToSave.size());
        }

        return errorMessages;
    }
}

