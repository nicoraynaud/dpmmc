package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.UtilisateurDao;
import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.RolesEnum;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import nc.noumea.mairie.dpmmc.services.interfaces.IAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthHelper implements IAuthHelper {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Override
    public boolean hasCurrentUserRole(RolesEnum role) {
        for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (ga.getAuthority().equals(role.toString()))
                return true;
        }
        return false;
    }

    @Override
    public String getCurrentUserIdentifiant() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Utilisateur getCurrentUser() {
        return utilisateurDao.getUtilisateur(getCurrentUserIdentifiant());
    }
}
