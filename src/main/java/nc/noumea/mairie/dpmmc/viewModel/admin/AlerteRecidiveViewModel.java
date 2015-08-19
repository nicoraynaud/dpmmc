package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.AlerteRecidive;
import nc.noumea.mairie.dpmmc.domain.NatureFait;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AlerteRecidiveViewModel extends ReferenceViewModel<AlerteRecidive> {

    public AlerteRecidiveViewModel() {
        super.clazz = AlerteRecidive.class;
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
