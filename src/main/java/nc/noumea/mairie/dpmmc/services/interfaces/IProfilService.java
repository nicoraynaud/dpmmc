package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.Profil;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface IProfilService {

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CREATION_FICHE"})
    List<Profil> getAllProfils();

    @Secured({"ROLE_ADMINISTRATION"})
    Profil save(Profil profil);
}
