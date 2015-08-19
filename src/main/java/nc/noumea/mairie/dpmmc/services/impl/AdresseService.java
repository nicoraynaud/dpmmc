package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.AdresseDao;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAdresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdresseService implements IAdresseService {

    @Autowired
    private AdresseDao adresseDao;

    @Override
    public List<Ville> getAllVilles() {
        return adresseDao.getAll(Ville.class, "libelle");
    }

    @Override
    public List<Secteur> getAllSecteursOfVille(Ville selectedVille) {
        return selectedVille == null ? null : adresseDao.getSecteursOfVille(selectedVille);
    }

    @Override
    public List<Quartier> getAllQuartiersOfSecteur(Secteur selectedSecteur) {
        return selectedSecteur == null ? null : adresseDao.getQuartiersOfSecteur(selectedSecteur);
    }

    @Override
    public List<NomVoie> getAllNomVoies() {
        return adresseDao.getAll(NomVoie.class, "libelle");
    }

    @Override
    public List<NomVoie> getAllNomVoiesOfQuartier(Quartier selectedQuartier) {
        return selectedQuartier == null ? null : adresseDao.getNomVoiesOfQuartier(selectedQuartier);
    }

    @Override
    public List<String> getAllNumeroOfNomVoie(NomVoie selectedNomVoie) {
        return selectedNomVoie == null ? null : adresseDao.getNumerosOfNomVoie(selectedNomVoie);
    }

    @Override
    public List<String> getAllNumeroOfNomVoieAndQuartier(NomVoie selectedNomVoie, Quartier quartier) {
        return selectedNomVoie == null ? null : quartier == null ? null : adresseDao.getNumerosOfNomVoie(selectedNomVoie, quartier);
    }

    @Override
    public List<Quartier> getAllQuartiersOfNomVoieAndNumero(NomVoie selectedNomVoie, String numero) {
        if (selectedNomVoie == null)
            return null;

        return adresseDao.getAllQuartiersOfNomVoieAndNumero(selectedNomVoie, numero);
    }

    @Override
    public Adresse getAdresseFromParams(NomVoie nomVoie, String numero, Quartier quartier) {
        if (nomVoie == null || numero == null || quartier == null)
            return null;

        return adresseDao.getAdresseFromParams(nomVoie, numero, quartier);
    }

    @Override
    public Adresse getAdresseFromLieuPredefini(LieuPredefini lieuPredefini) {
        return adresseDao.getAdresseFromLieuPredefini(lieuPredefini);
    }
}
