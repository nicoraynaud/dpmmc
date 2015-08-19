package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.*;

import java.util.List;

public interface IAdresseService {

    List<Ville> getAllVilles();
    List<Secteur> getAllSecteursOfVille(Ville selectedVille);
    List<Quartier> getAllQuartiersOfSecteur(Secteur selectedSecteur);
    List<NomVoie> getAllNomVoies();
    List<NomVoie> getAllNomVoiesOfQuartier(Quartier selectedQuartier);
    List<String> getAllNumeroOfNomVoie(NomVoie selectedNomVoie);
    List<String> getAllNumeroOfNomVoieAndQuartier(NomVoie selectedNomVoie, Quartier quartier);
    List<Quartier> getAllQuartiersOfNomVoieAndNumero(NomVoie selectedNomVoie, String numero);
    Adresse getAdresseFromParams(NomVoie nomVoie, String numero, Quartier quartier);
    Adresse getAdresseFromLieuPredefini(LieuPredefini lieuPredefini);
}

