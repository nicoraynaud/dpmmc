package nc.noumea.mairie.dpmmc.viewModel;

import java.util.List;

public class GeolocFicheListModel {

    private Long totalResults;
    private List<GeolocFicheModel> fiches;

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public List<GeolocFicheModel> getFiches() {
        return fiches;
    }

    public void setFiches(List<GeolocFicheModel> fiches) {
        this.fiches = fiches;
    }

    public GeolocFicheListModel(Long totalResults, List<GeolocFicheModel> fiches) {
        this.totalResults = totalResults;
        this.fiches = fiches;
    }
}
