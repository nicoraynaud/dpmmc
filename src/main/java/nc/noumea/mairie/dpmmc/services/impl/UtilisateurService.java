package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.FicheDao;
import nc.noumea.mairie.dpmmc.dao.UtilisateurDao;
import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import nc.noumea.mairie.dpmmc.services.interfaces.IAuthHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService implements IUtilisateurService {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDao.getUtilisateurs();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return (Utilisateur) utilisateurDao.save(utilisateur);
    }

}
