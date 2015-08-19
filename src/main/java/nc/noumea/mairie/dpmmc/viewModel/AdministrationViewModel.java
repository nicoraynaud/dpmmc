package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.viewModel.admin.ReferenceDataModel;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AdministrationViewModel {

    private List<ReferenceDataModel> dataToAdminister;

    private ReferenceDataModel selectedData;

    private String includedGrid;

    public List<ReferenceDataModel> getDataToAdminister() {
        return dataToAdminister;
    }

    public ReferenceDataModel getSelectedData() {
        return selectedData;
    }

    public void setSelectedData(ReferenceDataModel selectedData) {
        this.selectedData = selectedData;
    }

    public String getIncludedGrid() {
        return includedGrid;
    }

    public void setIncludedGrid(String includedGrid) {
        this.includedGrid = includedGrid;
    }

    @Init
    public void init() {
        dataToAdminister = new ArrayList<ReferenceDataModel>();
        dataToAdminister.add(new ReferenceDataModel("Adresses", Adresse.class));
        dataToAdminister.add(new ReferenceDataModel("Alertes en cas de récidives", AlerteRecidive.class));
        dataToAdminister.add(new ReferenceDataModel("Brigades", Brigade.class));
        dataToAdminister.add(new ReferenceDataModel("Catégories de fait", CategorieFait.class));
        dataToAdminister.add(new ReferenceDataModel("Catégories de personne", CategoriePersonne.class));
        dataToAdminister.add(new ReferenceDataModel("Catégories de procédure", CategorieProcedure.class));
        dataToAdminister.add(new ReferenceDataModel("Catégories de saisine", CategorieSaisine.class));
        dataToAdminister.add(new ReferenceDataModel("Contacts interpolice", ContactInterpolice.class));
        dataToAdminister.add(new ReferenceDataModel("Indicatifs radios", IndicatifRadio.class));
        dataToAdminister.add(new ReferenceDataModel("Lieux prédéfinis", LieuPredefini.class));
        dataToAdminister.add(new ReferenceDataModel("Natures de fait", NatureFait.class));
        dataToAdminister.add(new ReferenceDataModel("Paramètres applicatifs", ParametreApplication.class));
        dataToAdminister.add(new ReferenceDataModel("Véhicules", Vehicule.class));
    }

    @Command
    @NotifyChange("includedGrid")
    public void refreshAdminGrid() {
        includedGrid = String.format("includes/admin/%s.zul", selectedData.getClazz().getSimpleName().toLowerCase());
    }
}
