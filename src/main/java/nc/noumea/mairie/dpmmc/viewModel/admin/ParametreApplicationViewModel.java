package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.AlerteRecidive;
import nc.noumea.mairie.dpmmc.domain.NatureFait;
import nc.noumea.mairie.dpmmc.domain.ParametreApplication;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ParametreApplicationViewModel extends ReferenceViewModel<ParametreApplication> {

    public ParametreApplicationViewModel() {
        super.clazz = ParametreApplication.class;
    }

    private List<NatureFait> natureFaits;

    public List<NatureFait> getNatureFaits() {
        return natureFaits;
    }

    @Init
    public void init() {
        super.init();
        natureFaits = referenceService.getAll(NatureFait.class, "id");
    }

}
