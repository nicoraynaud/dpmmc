package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.CategorieSaisine;
import nc.noumea.mairie.dpmmc.domain.ContactInterpolice;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ContactInterpoliceViewModel extends ReferenceViewModel<ContactInterpolice> {

    public ContactInterpoliceViewModel() {
        super.clazz = ContactInterpolice.class;
        super.defaultField = "libelleLong";
        super.isFieldInParent = true;
    }

    @Init
    public void init() {
        super.init();
    }

}
