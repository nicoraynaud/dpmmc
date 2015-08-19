package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.CategorieFait;
import nc.noumea.mairie.dpmmc.domain.LieuPredefini;
import nc.noumea.mairie.dpmmc.domain.Patrouille;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class LieuPredefiniViewModel extends ReferenceViewModel<LieuPredefini> {

    public LieuPredefiniViewModel() {
        super.clazz = LieuPredefini.class;
        super.defaultField = "libelleLong";
        super.isFieldInParent = true;
    }

    @Init
    public void init() {
        super.init();
    }

    @Command
    public void editLieu(@BindingParam("lieu") LieuPredefini lieu) {
        Map<String, Object> args = new HashMap<>();
        args.put("lieu", lieu);
        Executions.createComponents("includes/admin/lieuPredefiniPopup.zul", null, args);
    }

    @GlobalCommand
    @NotifyChange({"data"})
    public void updateLieuPredefini(@BindingParam("lieu") LieuPredefini lieu) {
        super.itemChanged(lieu);
    }

}
