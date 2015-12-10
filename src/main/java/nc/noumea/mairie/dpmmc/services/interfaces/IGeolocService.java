package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.viewModel.GeolocFicheModel;

import java.util.List;

public interface IGeolocService {

    List<GeolocFicheModel> getAwaitingGeolocs();
    void updateAwaitingGeolocs();
    void removeFicheFromList(Long idGeolocalisationVP);
}
