package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.services.interfaces.IAppParametersService;
import nc.noumea.mairie.dpmmc.services.interfaces.IGeolocService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GeolocViewModel {

    @WireVariable
    private IGeolocService geolocService;

    @WireVariable
    private IAppParametersService appParametersService;

    private Integer refreshInterval;

    public Integer getRefreshInterval() {
        return refreshInterval;
    }

    private Integer nbFiches;

    public Integer getNbFiches() {
        return nbFiches;
    }

    private Long nbTotalFiches;

    public Long getNbTotalFiches() {
        return nbTotalFiches;
    }

    private Date lastRefreshDate;

    public Date getLastRefreshDate() {
        return lastRefreshDate;
    }

    private List<GeolocFicheModel> awaitingGeolocs;

    public List<GeolocFicheModel> getAwaitingGeolocs() {
        return awaitingGeolocs;
    }

    @Init
    public void initLists() {
        refreshInterval = appParametersService.getTicketsRefreshInterval();
        GeolocFicheListModel result = geolocService.getAwaitingGeolocs();
        awaitingGeolocs = result.getFiches();
        nbTotalFiches = result.getTotalResults();
        nbFiches = awaitingGeolocs.size();
        lastRefreshDate = new Date();
    }

    @Command
    @NotifyChange({ "awaitingGeolocs" })
    public void removeFromList(@BindingParam("geolocFiche") GeolocFicheModel fiche) {
        geolocService.removeFicheFromList(fiche.getIdGeolocalisationVP());
        awaitingGeolocs.remove(fiche);
    }

    @Command
    @NotifyChange({ "lastRefreshDate", "awaitingGeolocs", "nbTotalFiches", "nbFiches" })
    public void refreshList() {
        awaitingGeolocs.clear();
        GeolocFicheListModel result = geolocService.getAwaitingGeolocs();
        awaitingGeolocs = result.getFiches();
        nbTotalFiches = result.getTotalResults();
        nbFiches = awaitingGeolocs.size();
        lastRefreshDate = new Date();
    }
}
