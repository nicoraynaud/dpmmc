package nc.noumea.mairie.dpmmc.viewModel;


import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import org.springframework.util.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class IndexViewModel {

    @WireVariable
    private IFicheService ficheService;

    private ListModelList<TabModel> tabList;
    private List<String> tabIds;
    private TabModel selectedTab;

    public List<TabModel> getTabList() {
        return tabList;
    }

    public TabModel getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(TabModel selectedTab) {
        this.selectedTab = selectedTab;
    }

    @Wire("#tabbox")
    Tabbox tabbox;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }

    @Init
    public void init() {
        tabList = new ListModelList<TabModel>();
        tabIds = new ArrayList<String>();
        openTab("tabTbd", "Tableau de bord", "/includes/tdb.zul", false, "tdb-tab", null);
    }

    @NotifyChange("selectedTab")
    @Command
    public void selectTab(@BindingParam("item") TabModel tabModel){
        selectedTab = tabModel;
    }

    @Command
    @NotifyChange({ "tabList", "selectedTab" })
    public void openTab(@BindingParam("id") String id, @BindingParam("label") String label, @BindingParam("zulTemplate") String zulTemplate, @BindingParam("closable") boolean closable, @BindingParam("sclass") String sclass, @BindingParam("param") String param) {

        for (TabModel tm : tabList) {
            if (tm.getId().equals(id)) {
                selectedTab = tm;
                return;
            }
        }

        tabIds.add(id);
        TabModel tm = new TabModel(id, label, zulTemplate, closable, sclass, param);
        tabList.add(tm);
        selectedTab = tm;
    }

    @Command
    public void closingTab(@BindingParam("tab") final TabModel tabModel, @BindingParam("event") Object event) {

        if (tabModel == null)
            return;

        tabList.remove(tabModel);
        if (selectedTab != null && selectedTab.equals(tabModel) && !tabList.isEmpty()) {
            selectedTab = tabList.get(tabList.size() - 1);
        }
        tabIds.remove(tabModel.getId());
    }

    @Command
    @GlobalCommand
    @NotifyChange({ "tabList" })
    public void newFiche(@BindingParam("geolocFiche") GeolocFicheModel geolocFiche) {
        Long idFiche;
        if (geolocFiche != null) {
            // if exists, Call service to create new Fiche
            idFiche = ficheService.createFicheFromGeolocalisationVP(geolocFiche.getIdGeolocalisationVP());
        } else {
            // Create a new one to display
            idFiche = ficheService.createFicheFromBlank();
        }

        // Open it as a new tab
        openTab(idFiche.toString(), "Création de MC", "/includes/fiche.zul", true, "", idFiche.toString());
    }

    @Command
    @GlobalCommand
    @NotifyChange({ "tabList" })
    public void openFiche(@BindingParam("idFiche") Long idFiche) {
        openTab(idFiche.toString(), "Création de MC", "/includes/fiche.zul", true, "", idFiche.toString());
    }

    @Command
    @GlobalCommand
    public void setTabTitle(@BindingParam("idTab") String idTab, @BindingParam("title") String title) {

        if (idTab == null || StringUtils.isEmpty(title))
            return;

        for (TabModel t : tabList) {
            if (t.getId().equals(idTab)) {
                t.setLabel(title);
                break;
            }
        }

        for (Component c : tabbox.getTabs().getChildren()) {
            Tab t = (Tab) c;
            if (t.getId().equals(idTab)) {
                t.setLabel(title);
                break;
            }
        }
    }
}
