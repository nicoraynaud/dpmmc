package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.RolesEnum;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface IUtilisateurService {

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CREATION_FICHE"})
    public List<Utilisateur> getAllUtilisateurs();

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CREATION_FICHE"})
    public Utilisateur save(Utilisateur utilisateur);
}