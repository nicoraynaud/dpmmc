package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.Profil;
import nc.noumea.mairie.dpmmc.domain.Rattachement;
import nc.noumea.mairie.dpmmc.domain.Utilisateur;
import nc.noumea.mairie.dpmmc.services.interfaces.IProfilService;
import nc.noumea.mairie.dpmmc.services.interfaces.IUtilisateurService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UtilisateurPopupViewModel {

    @WireVariable
    private IUtilisateurService utilisateurService;

    @WireVariable
    private IProfilService profilService;

    private Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private List<Profil> profils;

    public List<Profil> getProfils() {
        return profils;
    }

    @Init
    public void init(@BindingParam("utilisateur") Utilisateur utilisateurArg) {
        utilisateur = utilisateurArg;
        profils = profilService.getAllProfils();
    }

    @Command
    public void save(@BindingParam("win") Window x) {

        if (!isDataValid()) {
            Messagebox.show("Des erreurs empêchent la sauvegarde de cet utilisateur.\nVeuillez les corriger avant de pouvoir sauver.",
                    "Erreur de sauvegarde", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        Utilisateur u = utilisateurService.save(utilisateur);
        utilisateur.setId(u.getId());

        x.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("utilisateur", utilisateur);
        BindUtils.postGlobalCommand(null, null, "updateUtilisateurDoneCommand", args);

        Clients.showNotification("L'utilisateur a été mis à jour avec succès.", "info", null, "bottom_right", 1500);
    }

    private boolean isDataValid() {

        if (StringUtils.isBlank(utilisateur.getIdentifiant())
                || utilisateur.getAgent().getMatricule() == 0
                || StringUtils.isBlank(utilisateur.getAgent().getNom())
                || StringUtils.isBlank(utilisateur.getAgent().getPrenom())
                || utilisateur.getProfil() == null)
            return false;

        return true;
    }

}
