package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.IndicatifRadio;
import nc.noumea.mairie.dpmmc.domain.Secteur;
import nc.noumea.mairie.dpmmc.domain.Ville;

import java.util.List;

public interface IReferenceService {

    <T> List<T> getAllActive(Class<T> c, String propOrderAsc);
    <T> List<T> getAll(Class<T> c, String propOrderAsc);

    List<IndicatifRadio> getIndicatifRadiosFromBrigade(Brigade brigade);

    <T> T save(Class<T> clazz, T item);
}
