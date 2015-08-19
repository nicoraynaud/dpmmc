package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.viewModel.GeolocFicheModel;

import java.util.List;

public interface IGeolocService {

    List<GeolocFicheModel> getAwaitingGeolocs();
    List<GeolocFicheModel> updateAndGetAwaitingGeolocs();
    void removeFicheFromList(Long idGeolocalisationVP);
}
