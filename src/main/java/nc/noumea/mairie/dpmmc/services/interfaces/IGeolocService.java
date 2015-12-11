package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.viewModel.GeolocFicheListModel;
import nc.noumea.mairie.dpmmc.viewModel.GeolocFicheModel;

import java.util.List;

public interface IGeolocService {

    GeolocFicheListModel getAwaitingGeolocs();
    void updateAwaitingGeolocs();
    void removeFicheFromList(Long idGeolocalisationVP);
}
