package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Fiche;
import nc.noumea.mairie.dpmmc.services.interfaces.IAppParametersService;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TbdViewModel {

    @WireVariable
    private IFicheService ficheService;

    @WireVariable
    private IAppParametersService appParametersService;

    private int pageSizeActivities;

    private int pageSizeCloturees;

    public int getPageSizeActivities() {
        return pageSizeActivities;
    }

    public int getPageSizeCloturees() {
        return pageSizeCloturees;
    }

    private ListModel<FicheSearchResultModel> lastActivities;

    public ListModel<FicheSearchResultModel> getLastActivities() {
        return lastActivities;
    }

    private ListModel<FicheSearchResultModel> lastFichesCloturees;

    public ListModel<FicheSearchResultModel> getLastFichesCloturees() {
        return lastFichesCloturees;
    }

    @Init
    @NotifyChange({ "pageSize", "lastActivities", "lastFichesCloturees" })
    public void initLists() {
        lastActivities = new ListModelList<FicheSearchResultModel>(ficheService.getLastActivitiesWithAlerte());
        lastFichesCloturees = new ListModelList<FicheSearchResultModel>(ficheService.getLastFichesCloturees());
        pageSizeCloturees = appParametersService.getTablePageSize();
        pageSizeActivities = appParametersService.getActivitesTablePageSize();
    }

}
