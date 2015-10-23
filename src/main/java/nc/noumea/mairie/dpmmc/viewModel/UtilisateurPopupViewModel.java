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

    private String newIdentifiant;
    private Profil newProfil;
    private boolean newActif;
    private String newNom;
    private String newPrenom;
    private Integer newMatricule;

    public String getNewIdentifiant() {
        return newIdentifiant;
    }

    public void setNewIdentifiant(String newIdentifiant) {
        this.newIdentifiant = newIdentifiant;
    }

    public Profil getNewProfil() {
        return newProfil;
    }

    public void setNewProfil(Profil newProfil) {
        this.newProfil = newProfil;
    }

    public boolean isNewActif() {
        return newActif;
    }

    public void setNewActif(boolean newActif) {
        this.newActif = newActif;
    }

    public String getNewNom() {
        return newNom;
    }

    public void setNewNom(String newNom) {
        this.newNom = newNom;
    }

    public String getNewPrenom() {
        return newPrenom;
    }

    public void setNewPrenom(String newPrenom) {
        this.newPrenom = newPrenom;
    }

    public Integer getNewMatricule() {
        return newMatricule;
    }

    public void setNewMatricule(Integer newMatricule) {
        this.newMatricule = newMatricule;
    }

    private String previousIdentifiant;
    private Profil previousProfil;
    private boolean previousActif;
    private String previousNom;
    private String previousPrenom;
    private Integer previousMatricule;
    
    @Init
    @NotifyChange({"newIdentifiant", "newActif", "newProfil", "newMatricule", "newNom", "newPrenom"})
    public void init(@BindingParam("utilisateur") Utilisateur utilisateurArg) {
        utilisateur = utilisateurArg;
        newIdentifiant = utilisateurArg.getIdentifiant();
        newActif = utilisateurArg.isActif();
        newProfil = utilisateurArg.getProfil();
        if (utilisateurArg.getAgent() != null) {
            newMatricule = utilisateurArg.getAgent().getMatricule();
            newNom = utilisateurArg.getAgent().getNom();
            newPrenom = utilisateurArg.getAgent().getPrenom();
        }

        previousIdentifiant = utilisateurArg.getIdentifiant();
        previousActif = utilisateurArg.isActif();
        previousProfil = utilisateurArg.getProfil();
        if (utilisateurArg.getAgent() != null) {
            previousMatricule = utilisateurArg.getAgent().getMatricule();
            previousNom = utilisateurArg.getAgent().getNom();
            previousPrenom = utilisateurArg.getAgent().getPrenom();
        }

        profils = profilService.getAllProfils();
    }

    @Command
    public void save(@BindingParam("win") Window x) {

        if (!isDataValid()) {
            Messagebox.show("Des erreurs empêchent la sauvegarde de cet utilisateur.\nVeuillez les corriger avant de pouvoir sauver.",
                    "Erreur de sauvegarde", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        utilisateur.setIdentifiant(newIdentifiant);
        utilisateur.setActif(newActif);
        utilisateur.setProfil(newProfil);
        utilisateur.getAgent().setMatricule(newMatricule);
        utilisateur.getAgent().setNom(newNom);
        utilisateur.getAgent().setPrenom(newPrenom);

        boolean wasNew = false;
        try {
            Utilisateur u = utilisateurService.save(utilisateur);
            wasNew = (utilisateur.getId() == null || utilisateur.getId() == 0);
            utilisateur.setId(u.getId());
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().toString().contains("ConstraintViolationException")) {
                Messagebox.show("L'identifiant que vous avez saisi est déjà utilisé.",
                        "Erreur de sauvegarde", Messagebox.OK, Messagebox.ERROR);
                utilisateur.setIdentifiant(previousIdentifiant);
                utilisateur.setActif(previousActif);
                utilisateur.setProfil(previousProfil);
                utilisateur.getAgent().setMatricule(previousMatricule);
                utilisateur.getAgent().setNom(previousNom);
                utilisateur.getAgent().setPrenom(previousPrenom);

                return;
            } else {
                throw new RuntimeException("Erreur lors de la sauvegarde, veuillez contacter votre administrateur.", ex);
            }
        }

        x.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("utilisateur", utilisateur);
        args.put("isNew", wasNew);
        BindUtils.postGlobalCommand(null, null, "updateUtilisateurDoneCommand", args);

        Clients.showNotification("L'utilisateur a été mis à jour avec succès.", "info", null, "bottom_right", 1500);
    }

    private boolean isDataValid() {

        if (StringUtils.isBlank(newIdentifiant)
                || newMatricule == null || newMatricule == 0
                || StringUtils.isBlank(newNom)
                || StringUtils.isBlank(newPrenom)
                || newProfil == null)
            return false;

        return true;
    }

}
