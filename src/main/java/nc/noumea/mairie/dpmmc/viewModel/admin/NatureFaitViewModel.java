package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.CategorieFait;
import nc.noumea.mairie.dpmmc.domain.NatureFait;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NatureFaitViewModel extends ReferenceViewModel<NatureFait> {

    public NatureFaitViewModel() {
        super.clazz = NatureFait.class;
        super.defaultField = "libelleLong";
        super.isFieldInParent = true;
    }

    private List<CategorieFait> categorieFaits;

    public List<CategorieFait> getCategorieFaits() {
        return categorieFaits;
    }

    @Init
    public void init() {
        super.init();
        categorieFaits = referenceService.getAll(CategorieFait.class, "ordre");
    }

}
