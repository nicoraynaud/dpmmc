package nc.noumea.mairie.dpmmc.viewModel;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAppParametersService;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheEventReportHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;

import java.util.Date;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RechercheViewModel {

    @WireVariable
    private IFicheService ficheService;

    @WireVariable
    private IReferenceService referenceService;

    @WireVariable
    private IAppParametersService appParametersService;

    private List<FicheSearchResultModel> searchResults;
    private String ficheNumero;
    private List<Agent> agents;
    private Agent selectedAgent;
    private ContainsSimpleListModel<Brigade> brigades;
    private Brigade selectedBrigade;
    private ContainsSimpleListModel<NatureFait> natureFaits;
    private NatureFait selectedNature;
    private ContainsSimpleListModel<CategorieFait> categorieFaits;
    private CategorieFait selectedCategorieFait;
    private ContainsSimpleListModel<Quartier> quartiers;
    private Quartier selectedQuartier;
    private ContainsSimpleListModel<Secteur> secteurs;
    private Secteur selectedSecteur;
    private ContainsSimpleListModel<LieuPredefini> lieuxPredefinis;
    private LieuPredefini selectedLieu;
    private ContainsSimpleListModel<CategoriePersonne> categoriePersonnes;
    private CategoriePersonne selectedCategoriePersonne;
    private String nomPersonne;
    private Date dateSaisieStart;
    private Date dateSaisieEnd;

    private int pageSize;

    public ContainsSimpleListModel<Brigade> getBrigades() {
        return brigades;
    }

    public ContainsSimpleListModel<NatureFait> getNatureFaits() {
        return natureFaits;
    }

    public ContainsSimpleListModel<CategorieFait> getCategorieFaits() {
        return categorieFaits;
    }

    public ContainsSimpleListModel<Quartier> getQuartiers() {
        return quartiers;
    }

    public ContainsSimpleListModel<Secteur> getSecteurs() {
        return secteurs;
    }

    public ContainsSimpleListModel<CategoriePersonne> getCategoriePersonnes() {
        return categoriePersonnes;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<FicheSearchResultModel> getSearchResults() {
        return searchResults;
    }

    public String getFicheNumero() {
        return ficheNumero;
    }

    public void setFicheNumero(String ficheNumero) {
        this.ficheNumero = ficheNumero;
    }

    public Agent getSelectedAgent() {
        return selectedAgent;
    }

    public void setSelectedAgent(Agent selectedAgent) {
        this.selectedAgent = selectedAgent;
    }

    public Brigade getSelectedBrigade() {
        return selectedBrigade;
    }

    public void setSelectedBrigade(Brigade selectedBrigade) {
        this.selectedBrigade = selectedBrigade;
    }

    public NatureFait getSelectedNature() {
        return selectedNature;
    }

    public void setSelectedNature(NatureFait selectedNature) {
        this.selectedNature = selectedNature;
    }

    public CategorieFait getSelectedCategorieFait() {
        return selectedCategorieFait;
    }

    public void setSelectedCategorieFait(CategorieFait selectedCategorieFait) {
        this.selectedCategorieFait = selectedCategorieFait;
    }

    public Quartier getSelectedQuartier() {
        return selectedQuartier;
    }

    public void setSelectedQuartier(Quartier selectedQuartier) {
        this.selectedQuartier = selectedQuartier;
    }

    public Secteur getSelectedSecteur() {
        return selectedSecteur;
    }

    public void setSelectedSecteur(Secteur selectedSecteur) {
        this.selectedSecteur = selectedSecteur;
    }

    public ContainsSimpleListModel<LieuPredefini> getLieuxPredefinis() {
        return lieuxPredefinis;
    }

    public LieuPredefini getSelectedLieu() {
        return selectedLieu;
    }

    public void setSelectedLieu(LieuPredefini selectedLieu) {
        this.selectedLieu = selectedLieu;
    }

    public CategoriePersonne getSelectedCategoriePersonne() {
        return selectedCategoriePersonne;
    }

    public void setSelectedCategoriePersonne(CategoriePersonne selectedCategoriePersonne) {
        this.selectedCategoriePersonne = selectedCategoriePersonne;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

    public void setNomPersonne(String nomPersonne) {
        this.nomPersonne = nomPersonne;
    }

    public Date getDateSaisieStart() {
        return dateSaisieStart;
    }

    public void setDateSaisieStart(Date dateSaisieStart) {
        this.dateSaisieStart = dateSaisieStart;
    }

    public Date getDateSaisieEnd() {
        return dateSaisieEnd;
    }

    public void setDateSaisieEnd(Date dateSaisieEnd) {
        this.dateSaisieEnd = dateSaisieEnd;
    }


    public int getPageSize() {
        return pageSize;
    }

    @Init
    public void initLists() {
        agents = referenceService.getAll(Agent.class, "nom");
        brigades = new ContainsSimpleListModel(referenceService.getAllActive(Brigade.class, "libelleCourt"), Brigade.class, "libelleCourt");
        natureFaits = new ContainsSimpleListModel(referenceService.getAllActive(NatureFait.class, "libelleLong"), NatureFait.class, "libelleLong");
        categorieFaits = new ContainsSimpleListModel(referenceService.getAllActive(CategorieFait.class, "libelleLong"), CategorieFait.class, "libelleLong");
        secteurs = new ContainsSimpleListModel(referenceService.getAll(Secteur.class, "libelle"), Secteur.class, "libelle");
        quartiers = new ContainsSimpleListModel(referenceService.getAll(Quartier.class, "libelle"), Quartier.class, "libelle");
        categoriePersonnes = new ContainsSimpleListModel(referenceService.getAllActive(CategoriePersonne.class, "libelleLong"), CategoriePersonne.class, "libelleLong");
        lieuxPredefinis = new ContainsSimpleListModel(referenceService.getAllActive(LieuPredefini.class, "libelleLong"), LieuPredefini.class, "libelleLong");
        pageSize = appParametersService.getTablePageSize();
    }

    @Command
    @NotifyChange({ "searchResults" })
    public void search() {
        searchResults = ficheService.search(
                ficheNumero,
                selectedAgent != null ? selectedAgent.getId() : null,
                selectedBrigade != null ? selectedBrigade.getId() : null,
                selectedNature != null ? selectedNature.getId() : null,
                selectedCategorieFait != null ? selectedCategorieFait.getId() : null,
                selectedSecteur != null ? selectedSecteur.getId() : null,
                selectedQuartier != null ? selectedQuartier.getId() : null,
                selectedLieu != null ? selectedLieu.getId() : null,
                selectedCategoriePersonne != null ? selectedCategoriePersonne.getId() : null,
                nomPersonne, dateSaisieStart, dateSaisieEnd);
    }

    @Command
    @NotifyChange({"ficheNumero",
            "selectedAgent",
            "selectedBrigade",
            "selectedNature",
            "selectedCategorieFait",
            "selectedQuartier",
            "selectedSecteur",
            "selectedLieu",
            "selectedCategoriePersonne",
            "nomPersonne",
            "dateSaisieStart",
            "dateSaisieEnd"})
    public void clearForm() {
        ficheNumero = null;
        selectedAgent = null;
        selectedBrigade = null;
        selectedNature = null;
        selectedCategorieFait = null;
        selectedQuartier = null;
        selectedSecteur = null;
        selectedLieu = null;
        selectedCategoriePersonne = null;
        nomPersonne = null;
        dateSaisieStart = null;
        dateSaisieEnd = null;
    }


    @Command
    public void exportReport() {

        byte[] reportAsBytes = ficheService.exportFicheEventReport(
                ficheNumero,
                selectedAgent != null ? selectedAgent.getId() : null,
                selectedBrigade != null ? selectedBrigade.getId() : null,
                selectedNature != null ? selectedNature.getId() : null,
                selectedCategorieFait != null ? selectedCategorieFait.getId() : null,
                selectedSecteur != null ? selectedSecteur.getId() : null,
                selectedQuartier != null ? selectedQuartier.getId() : null,
                selectedLieu != null ? selectedLieu.getId() : null,
                selectedCategoriePersonne != null ? selectedCategoriePersonne.getId() : null,
                nomPersonne, dateSaisieStart, dateSaisieEnd);
        Filedownload.save(reportAsBytes, "application/pdf", String.format("fiche_evenementielle_%s", new DateTime().toString("dd-MM-yyyy")));

    }
}
