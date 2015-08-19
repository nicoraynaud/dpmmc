package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.GenericDao;
import nc.noumea.mairie.dpmmc.dao.ReferenceDao;
import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.IndicatifRadio;
import nc.noumea.mairie.dpmmc.domain.Secteur;
import nc.noumea.mairie.dpmmc.domain.Ville;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReferenceService implements IReferenceService {

    @Autowired
    private GenericDao genericDao;

    @Autowired
    private ReferenceDao referenceDao;

    @Override
    public <T> List<T> getAllActive(Class<T> c, String propOderAsc) {
        return genericDao.getAllActive(c, propOderAsc);
    }

    @Override
    public <T> List<T> getAll(Class<T> c, String propOderAsc) {
        return genericDao.getAll(c, propOderAsc);
    }

    @Override
    public List<IndicatifRadio> getIndicatifRadiosFromBrigade(Brigade brigade) {
        return referenceDao.getIndicatifRadiosFromBrigade(brigade);
    }

    @Override
    public <T> T save(Class<T> clazz, T item) {
        return (T) genericDao.save(item);
    }
}
