package nc.noumea.mairie.dpmmc.viewModel;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAdresseService;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.springframework.util.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.*;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FicheViewModel {

    @WireVariable
    private IFicheService ficheService;

    @WireVariable
    private IReferenceService referenceService;

    @WireVariable
    private IAdresseService adresseService;

    private FicheModel fiche;

    public FicheModel getFiche() {
        return fiche;
    }

    private AgentSimpleListModel agents;
    private Agent selectedAgent;

    private ContainsSimpleListModel<LieuPredefini> lieuxPredefinis;
    private List<Ville> villes;
    private Ville selectedVille;
    private ContainsSimpleListModel<Secteur> secteurs;
    private Secteur selectedSecteur;
    private ContainsSimpleListModel<Quartier> quartiers;
    private Quartier selectedQuartier;
    private ContainsSimpleListModel<NomVoie> nomVoies;
    private NomVoie selectedNomVoie;
    private List<String> numeros;
    private String numero;

    private List<CategorieSaisine> categorieSaisinesRawList;
    private List<ContactInterpolice> contactInterpolicesRawList;

    private HashMap<String, String> formErrors = new HashMap<>();

    public Set<Patrouille> getFichePatrouilles() {
        return fiche != null ? fiche.getFiche().getPatrouilles() : null;
    }

    public Set<Appelant> getFicheAppelants() {
        return fiche != null ? fiche.getFiche().getAppelants() : null;
    }

    public Set<Fait> getFicheFaitss() {
        return fiche != null ? fiche.getFiche().getFaits() : null;
    }

    public Set<Fait> getFicheFaits() {
        return fiche != null ? fiche.getFiche().getFaits() : null;
    }

    public Set<Personne> getFichePersonnes() {
        return fiche != null ? fiche.getFiche().getPersonnes() : null;
    }

    public Set<Procedure> getFicheProcedures() {
        return fiche != null ? fiche.getFiche().getProcedures() : null;
    }

    public Agent getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(Agent selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public String getSelectedVille() {
        return fiche.getFiche().getAdresse() == null ? "" : fiche.getFiche().getAdresse().getQuartier().getSecteur().getVille().getLibelle();
    }
    public String getSelectedSecteur() {
        return fiche.getFiche().getAdresse() == null ? "" : fiche.getFiche().getAdresse().getQuartier().getSecteur().getLibelle();
    }
    public String getSelectedQuartier() {
        return fiche.getFiche().getAdresse() == null ? "" : fiche.getFiche().getAdresse().getQuartier().getLibelle();
    }
    public String getSelectedNomVoie() {
        return fiche.getFiche().getAdresse() == null ? "" : fiche.getFiche().getAdresse().getNomVoie().getLibelle();
    }
    public String getNumero() {
        return fiche.getFiche().getAdresse() == null ? "" : fiche.getFiche().getAdresse().getNumeroVoie();
    }

    public LieuPredefini getSelectedLieuPredefini() {
        return fiche.getFiche().getLieuPredefini();
    }

    public void setSelectedLieuPredefini(LieuPredefini lieuPredefini) {
        fiche.getFiche().setLieuPredefini(lieuPredefini);
    }

    public AgentSimpleListModel getAgents() {
        return agents;
    }

    public ContainsSimpleListModel<LieuPredefini> getLieuxPredefinis() {
        return lieuxPredefinis;
    }

    public ContainsSimpleListModel<ContactInterpolice> getContactInterpolices() {
        return new ContainsSimpleListModel(contactInterpolicesRawList, ContactInterpolice.class, "libelleLong");
    }

    public ContainsSimpleListModel getCategorieSaisines() {
        return new ContainsSimpleListModel(categorieSaisinesRawList, CategorieSaisine.class, "libelleLong");
    }

    public HashMap<String, String> getFormErrors() {
        return formErrors;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Init
    public void initLists(@ExecutionArgParam("param") String idFiche) {

        Long idFicheLong;

        agents = new AgentSimpleListModel(referenceService.getAll(Agent.class, "nom"));
        categorieSaisinesRawList = referenceService.getAllActive(CategorieSaisine.class, "libelleLong");
        contactInterpolicesRawList = referenceService.getAllActive(ContactInterpolice.class, "libelleLong");

        // Retrieve MC
        idFicheLong = Long.parseLong(idFiche);
        fiche = ficheService.getFiche(idFicheLong);

        lieuxPredefinis = new ContainsSimpleListModel(referenceService.getAllActive(LieuPredefini.class, "libelleLong"), LieuPredefini.class, "libelleLong");

        // Set tab title
        HashMap<String, Object> args = new HashMap<>();
        args.put("idTab", idFicheLong.toString());
        args.put("title", String.format("%s %s", "MC ", fiche.getFiche().getReference()));
        BindUtils.postGlobalCommand(null, null, "setTabTitle", args);
    }

    @Command
    @NotifyChange({"selectedVille", "selectedSecteur", "selectedQuartier", "selectedNomVoie", "numero"})
    public void refreshLieuPredefini() {

        // If none selected, reinit fields as for creation
        if (fiche.getFiche().getLieuPredefini() == null) {
            fiche.getFiche().setAdresse(null);
        } else {
            Adresse ad = fiche.getFiche().getLieuPredefini().getAdresse();
            fiche.getFiche().setAdresse(ad);
        }
    }

    @Command
    public void editAdresse() {
        Map<String, Object> args = new HashMap<>();
        args.put("adresse", fiche.getFiche().getAdresse());
        args.put("idFiche", fiche.getFiche().getId());
        Executions.createComponents("includes/admin/lieuPredefiniPopup.zul", null, args);
    }

    @NotifyChange({"selectedVille", "selectedSecteur", "selectedQuartier", "selectedNomVoie", "numero", "selectedLieuPredefini"})
    @GlobalCommand
    public void updateAdresse(@BindingParam("adresse") Adresse adresse, @BindingParam("idFiche") Long idFiche) {
        if (idFiche == fiche.getFiche().getId()) {
            fiche.getFiche().setAdresse(adresse);
            fiche.getFiche().setLieuPredefini(null);
        }
    }

    @Command
    public void addEquipageCommand() {
        Map<String, Object> args = new HashMap<>();
        args.put("equipages", fiche.getFiche().getPatrouilles());
        args.put("ficheId", fiche.getFiche().getId());
        Executions.createComponents("includes/equipagePopup.zul", null, args);
    }

    @GlobalCommand
    @NotifyChange({"fichePatrouilles"})
    public void updateEquipagesCommand(@BindingParam("equipages") List<Patrouille> patrouilles, @BindingParam("ficheId") Long ficheId) {

        if (!this.getFiche().getFiche().getId().equals(ficheId))
            return;

        fiche.getFiche().getPatrouilles().clear();

        for (Patrouille p : patrouilles) {
            fiche.getFiche().addPatrouille(p);
        }
    }

    @Command
    @NotifyChange({"ficheAppelants"})
    public void addAppelantCommand() {
        fiche.getFiche().addAppelant(new Appelant());
    }

    @Command
    @NotifyChange({"ficheAppelants"})
    public void removeAppelantCommand(@BindingParam("item") Appelant appelant) {
        fiche.getFiche().getAppelants().remove(appelant);
    }

    @Command
    @NotifyChange({"ficheFaits"})
    public void addFaitCommand() {
        fiche.getFiche().addFait(new Fait());
    }

    @Command
    @NotifyChange({"ficheFaits"})
    public void removeFaitCommand(@BindingParam("item") Fait fait) {
        fiche.getFiche().getFaits().remove(fait);
    }

    @Command
    @NotifyChange({"ficheFaits"})
    public void natureFaitChangedCommand(@BindingParam("item") Fait fait, @BindingParam("selectedNatureFait") NatureFait selectedValue) {
        fait.setNature(selectedValue);
    }

    @Command
    @NotifyChange({"fichePersonnes"})
    public void addPersonneCommand() {
        fiche.getFiche().addPersonne(new Personne());
    }

    @Command
    @NotifyChange({"fichePersonnes"})
    public void removePersonneCommand(@BindingParam("item") Personne personne) {
        fiche.getFiche().getPersonnes().remove(personne);
    }

    @Command
    @NotifyChange({"ficheProcedures"})
    public void addProcedureCommand() {
        fiche.getFiche().addProcedure(new Procedure());
    }

    @Command
    @NotifyChange({"ficheProcedures"})
    public void removeProcedureCommand(@BindingParam("item") Procedure procedure) {
        fiche.getFiche().getProcedures().remove(procedure);
    }

    @Command
    @NotifyChange({"formErrors"})
    public boolean save() {

        if (!validateForm()) {
            StringBuilder sb = new StringBuilder();
            for (String s : formErrors.values()) {
                sb.append("- " + s + "\n");
            }
            Messagebox.show("Des erreurs empêchent la sauvegarde de cette fiche.\nVeuillez les corriger avant de pouvoir sauver.\n\n" + sb.toString(),
                    "Erreur de sauvegarde", Messagebox.OK, Messagebox.EXCLAMATION);

            return false;
        }

        ficheService.saveFiche(fiche.getFiche());
        Clients.showNotification("La fiche a été mise à jour avec succès.", "info", null, "bottom_right", 1500);
        // Reload the Fiche in order to fix a bug where even if the Fiche and its children elements are saved, the ids of the children elements are not
        // properly set by hibernate after the merge. Therefore, forcing a reload ensures all the children elements have their id set and will not be
        // duplicated in further saves.
        initLists(this.fiche.getFiche().getId().toString());
        return true;
    }

    @Command
    @NotifyChange({"*", "fiche"})
    public void delete() {
        Map<String, Object> args = new HashMap<>();
        args.put("ficheId", this.getFiche().getFiche().getId());

        Executions.createComponents("includes/deleteFicheConfirmationPopup.zul", null, args);
    }

    @NotifyChange({"*", "fiche"})
    @GlobalCommand("performFicheDeleteCommand")
    public void performDelete(@BindingParam("motif") String motif, @BindingParam("ficheId") Long ficheId) {

        if (!this.getFiche().getFiche().getId().equals(ficheId))
            return;

        ficheService.supprimeFiche(this.fiche.getFiche().getId(), motif);
        initLists(this.fiche.getFiche().getId().toString());
    }

    @Command
    @NotifyChange({"*", "fiche"})
    public void cloturer() {

        if (!save()) {
            return;
        }

        ficheService.clotureFiche(this.fiche.getFiche().getId());
        initLists(this.fiche.getFiche().getId().toString());
    }

    @Command
    @NotifyChange({"*", "fiche"})
    public void decloturer() {
        ficheService.declotureFiche(this.fiche.getFiche().getId());
        initLists(this.fiche.getFiche().getId().toString());
    }

    protected boolean validateForm() {

        formErrors.clear();

        if (fiche.getFiche().getCategorieSaisine() == null)
            formErrors.put("categorieSaisine", "La catégorie de saisine est obligatoire.");

        if (fiche.getFiche().getContactInterpolice() == null)
            formErrors.put("contactInterpolice", "Le contact interpolice est obligatoire.");

        if (StringUtils.isEmpty(fiche.getFiche().getGdhAppel()))
            formErrors.put("gdhAppel", "GDH Appel est obligatoire.");

        if (StringUtils.isEmpty(fiche.getFiche().getSituationInitialeTitre()))
            formErrors.put("situationInitialeTitre", "Le titre de situation initiale est requis.");

        if (fiche.getFiche().getPatrouilles().isEmpty())
            formErrors.put("equipages", "Au moins un équipage doit être renseigné.");

        for (Appelant a : fiche.getFiche().getAppelants()) {
            if (StringUtils.isEmpty(a.getRequerant())) {
                formErrors.put("appelants", "Au moins un TPH appelant n'a pas de nom.");
                break;
            }
        }

        for (Fait f : fiche.getFiche().getFaits()) {
            if (f.getNature() == null || f.getNombre() == null) {
                formErrors.put("faits", "Au moins un Fait renseigné n'a pas de catégorie ou de nombre.");
                break;
            }
        }
        if (fiche.getFiche().getFaits().isEmpty()) {
            formErrors.put("faits", "Vous devez saisir au moins un fait pour cette Main Courante.");
        }

        for (Personne p : fiche.getFiche().getPersonnes()) {
            if (p.getCategorie() == null || StringUtils.isEmpty(p.getDetails()) || StringUtils.isEmpty(p.getNom())) {
                formErrors.put("personnes", "Au moins une Personne renseignée n'a pas de catégorie ou pas de nom ni de détail de rempli.");
                break;
            }
        }

        for (Procedure p : fiche.getFiche().getProcedures()) {
            if (p.getCategorie() == null || StringUtils.isEmpty(p.getNumero())) {
                formErrors.put("procedures", "Au moins une procédure renseignée n'a pas de catégorie ou de référence.");
                break;
            }
        }

        return formErrors.isEmpty();
    }

    @Command
    public void printFiche() {

        // If this Fiche has not yet been saved, dont bother create the report with it !
        if (StringUtils.isEmpty(fiche.getFiche().getReference()))
            return;

        if (this.getFiche().getFiche().getStatut() == StatutEnum.NON_CLOTUREE && !save()) {
            return;
        }

        try {
            byte[] reportAsBytes = ficheService.getFicheReport(fiche.getFiche().getId());
            Filedownload.save(reportAsBytes, "application/pdf", String.format("mc_%s", fiche.getFiche().getReference()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
