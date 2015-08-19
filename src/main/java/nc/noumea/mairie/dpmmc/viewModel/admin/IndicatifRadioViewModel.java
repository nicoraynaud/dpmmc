package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.IndicatifRadio;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class IndicatifRadioViewModel extends ReferenceViewModel<IndicatifRadio> {

    public IndicatifRadioViewModel() {
        super.clazz = IndicatifRadio.class;
        super.defaultField = "libelleLong";
        super.isFieldInParent = true;
    }

    @WireVariable
    private IReferenceService referenceService;

    private List<Brigade> brigades;

    public List<Brigade> getBrigades() {
        return brigades;
    }

    @Init
    public void init() {
        super.init();
        brigades = referenceService.getAll(Brigade.class, "id");
    }

}
