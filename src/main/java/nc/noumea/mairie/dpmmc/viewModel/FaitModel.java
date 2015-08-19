package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.NatureFait;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FaitModel {

    @WireVariable
    private IReferenceService referenceService;

    private ContainsSimpleListModel<NatureFait> natureFaits;
    public ContainsSimpleListModel<NatureFait> getNatureFaits() {
        return natureFaits;
    }

    @Init
    public void init() {
        natureFaits =  new ContainsSimpleListModel(referenceService.getAllActive(NatureFait.class, "categorieFait"), NatureFait.class, "libelleLong");
    }
}
