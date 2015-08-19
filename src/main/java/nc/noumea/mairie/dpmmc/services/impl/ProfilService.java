package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.ProfilDao;
import nc.noumea.mairie.dpmmc.dao.UtilisateurDao;
import nc.noumea.mairie.dpmmc.domain.Profil;
import nc.noumea.mairie.dpmmc.services.interfaces.IProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfilService implements IProfilService {

    @Autowired
    private ProfilDao profilDao;

    @Override
    public List<Profil> getAllProfils() {
        return profilDao.getAllProfils();
    }

    @Override
    public Profil save(Profil profil) {
        return (Profil) profilDao.save(profil);
    }
}
