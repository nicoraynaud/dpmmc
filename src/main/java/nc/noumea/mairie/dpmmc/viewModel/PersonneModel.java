package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.CategoriePersonne;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PersonneModel {

    @WireVariable
    private IReferenceService referenceService;

    private ContainsSimpleListModel<CategoriePersonne> categoriesPersonnes;
    public ContainsSimpleListModel<CategoriePersonne> getCategoriesPersonnes() {
        return categoriesPersonnes;
    }

    @Init
    public void init() {
        categoriesPersonnes =  new ContainsSimpleListModel(referenceService.getAllActive(CategoriePersonne.class, "libelleLong"), CategoriePersonne.class, "libelleLong");
    }
}
