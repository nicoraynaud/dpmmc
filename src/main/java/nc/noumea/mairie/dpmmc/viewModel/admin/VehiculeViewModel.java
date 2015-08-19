package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.Vehicule;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class VehiculeViewModel extends ReferenceViewModel<Vehicule> {

    public VehiculeViewModel() {
        super.clazz = Vehicule.class;
        super.defaultField = "type";
        super.isFieldInParent = false;
    }

    @Init
    public void init() {
        super.init();
    }

}
