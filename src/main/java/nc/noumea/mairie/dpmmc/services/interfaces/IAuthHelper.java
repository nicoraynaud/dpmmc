package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.RolesEnum;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;

public interface IAuthHelper {

    public boolean hasCurrentUserRole(RolesEnum role);
    public String getCurrentUserIdentifiant();
    public Utilisateur getCurrentUser();

}
