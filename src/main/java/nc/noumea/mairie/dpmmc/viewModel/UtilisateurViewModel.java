package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.Profil;
import nc.noumea.mairie.dpmmc.domain.Rattachement;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import nc.noumea.mairie.dpmmc.services.interfaces.IProfilService;
import nc.noumea.mairie.dpmmc.services.interfaces.IUtilisateurService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UtilisateurViewModel {

    @WireVariable
    private IUtilisateurService utilisateurService;

    @WireVariable
    private IProfilService profilService;

    private ListModel<Utilisateur> utilisateurs;

    public ListModel<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    @Init
    @NotifyChange({ "utilisateurs" })
    public void initLists() {
        utilisateurs = new ListModelList<Utilisateur>(utilisateurService.getAllUtilisateurs());
    }

    @Command
    public void itemChanged(@BindingParam("item") Utilisateur utilisateur) {
        Utilisateur u = utilisateurService.save(utilisateur);
        utilisateur.setId(u.getId());

        Clients.showNotification("L'utilisateur a été mis à jour avec succès.", "info", null, "bottom_right", 1500);
    }

    @Command
    public void createUtilisateur() {
        Utilisateur u = new Utilisateur();
        Agent a = new Agent();
        u.setAgent(a);
        u.setIdentifiant("Nouveau user #" + (utilisateurs.getSize() + 1));
        ((ListModelList) utilisateurs).add(0, u);
    }

    @Command
    public void updateRattachements(@BindingParam("item") Utilisateur utilisateur) {
        Map<String, Object> args = new HashMap<>();
        args.put("utilisateur", utilisateur);
        Executions.createComponents("includes/rattachementsPopup.zul", null, args);
    }

    @GlobalCommand
    public void updateUtilisateurRattachementsCommand(@BindingParam("utilisateur") Utilisateur utilisateur) {
        itemChanged(utilisateur);
    }

    @Command
    public void updateUtilisateur(@BindingParam("item") Utilisateur utilisateur) {
        Map<String, Object> args = new HashMap<>();
        args.put("utilisateur", utilisateur);
        Executions.createComponents("includes/utilisateurPopup.zul", null, args);
    }

    @GlobalCommand
    public void updateUtilisateurDoneCommand(@BindingParam("utilisateur") Utilisateur utilisateur) {
        BindUtils.postNotifyChange(null, null, utilisateur, ".");
    }

}
