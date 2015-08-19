package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.CategorieProcedure;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProcedureModel {

    @WireVariable
    private IReferenceService referenceService;

    private ContainsSimpleListModel<CategorieProcedure> categorieProcedures;
    public ContainsSimpleListModel<CategorieProcedure> getCategorieProcedures() {
        return categorieProcedures;
    }

    @Init
    public void init() {
        categorieProcedures =  new ContainsSimpleListModel(referenceService.getAllActive(CategorieProcedure.class, "libelleLong"), CategorieProcedure.class, "libelleLong");
    }
}
