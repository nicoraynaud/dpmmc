package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.Rattachement;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RattachementViewModel {

    @WireVariable
    private IReferenceService referenceService;

    private Utilisateur utilisateur;

    private ListModelList<Rattachement> rattachements;

    public List<Rattachement> getRattachements() {
        return rattachements;
    }

    private List<Brigade> brigades;

    public List<Brigade> getBrigades() {
        return brigades;
    }

    @Init
    public void init(@BindingParam("utilisateur") Utilisateur utilisateurArg) {

        brigades = referenceService.getAll(Brigade.class, "libelleCourt");

        utilisateur = utilisateurArg;
        rattachements = new ListModelList<>(utilisateur.getAgent().getRattachements());
    }

    @Command
    public void addRattachement() {
        Rattachement r = new Rattachement();
        r.setAgent(utilisateur.getAgent());
        ((ListModelList) rattachements).add(0, r);
    }

    @Command
    @NotifyChange({"rattachements"})
    public void removeRattachement(@BindingParam("item") Rattachement rattachement) {
        if (rattachement == null)
            return;

        Rattachement toRemove = null;

        for (Rattachement r : rattachements) {

            if (rattachement.getId() == null && rattachement.equals(r)) {
                toRemove = r;
                break;
            }

            if (r.getId() != null && r.getId().equals(rattachement.getId())) {
                toRemove = r;
                break;
            }

        }

        if (toRemove != null) {
            rattachements.remove(toRemove);
        }
    }

    @Command
    public void save(@BindingParam("win") Window x) {

        if (!isDataValid()) {
            Messagebox.show("Des erreurs empêchent la sauvegarde de ces rattachements.\nVeuillez les corriger avant de pouvoir sauver.",
                    "Erreur de sauvegarde", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        utilisateur.getAgent().getRattachements().clear();
        for (Rattachement r : rattachements) {
            utilisateur.getAgent().getRattachements().add(r);
        }

        x.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("utilisateur", utilisateur);
        BindUtils.postGlobalCommand(null, null, "updateUtilisateurRattachementsCommand", args);
    }

    public boolean isDataValid() {

        for (Rattachement r : rattachements) {
            if (r.getBrigade() == null || r.getDebut() == null)
                return false;
        }

        return true;
    }
}
