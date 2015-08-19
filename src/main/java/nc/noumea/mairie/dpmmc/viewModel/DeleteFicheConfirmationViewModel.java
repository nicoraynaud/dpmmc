package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Rattachement;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

public class DeleteFicheConfirmationViewModel {

    private Long ficheId;

    private String motif;

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Init
    public void init(@BindingParam("ficheId") Long ficheId) {
        this.ficheId = ficheId;
    }

    @Command
    public void delete(@BindingParam("win") Window x) {

        if (StringUtils.isEmpty(motif)) {
            Messagebox.show("La saisie d'un motif de suppression est obligatoire.",
                    "Erreur de suppression", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        x.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("motif", motif);
        args.put("ficheId", ficheId);
        BindUtils.postGlobalCommand(null, null, "performFicheDeleteCommand", args);
    }
}
