package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.CategorieFait;
import nc.noumea.mairie.dpmmc.domain.CategorieSaisine;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CategorieFaitViewModel extends ReferenceViewModel<CategorieFait> {

    public CategorieFaitViewModel() {
        super.clazz = CategorieFait.class;
        super.defaultField = "libelleLong";
        super.isFieldInParent = true;
    }

    @Init
    public void init() {
        super.init();
    }

}
