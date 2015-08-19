package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAdresseService;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import nc.noumea.mairie.dpmmc.viewModel.ContainsSimpleListModel;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class LieuPredefiniEditViewModel {

    @WireVariable
    private IAdresseService adresseService;

    private LieuPredefini lieuPredefini;
    private Adresse adresse;
    private Long idFiche;

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

    public LieuPredefini getLieuPredefini() {
        return lieuPredefini;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public Ville getSelectedVille() {
        return selectedVille;
    }

    public void setSelectedVille(Ville selectedVille) {
        this.selectedVille = selectedVille;
    }

    public Secteur getSelectedSecteur() {
        return selectedSecteur;
    }

    public void setSelectedSecteur(Secteur selectedSecteur) {
        this.selectedSecteur = selectedSecteur;
    }

    public NomVoie getSelectedNomVoie() {
        return selectedNomVoie;
    }

    public void setSelectedNomVoie(NomVoie selectedNomVoie) {
        this.selectedNomVoie = selectedNomVoie;
    }

    public Quartier getSelectedQuartier() {
        return selectedQuartier;
    }

    public void setSelectedQuartier(Quartier selectedQuartier) {
        this.selectedQuartier = selectedQuartier;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public ContainsSimpleListModel<Secteur> getSecteurs() {
        return secteurs;
    }

    public ContainsSimpleListModel<Quartier> getQuartiers() {
        return quartiers;
    }

    public ContainsSimpleListModel<NomVoie> getNomVoies() {
        return nomVoies;
    }

    public List<String> getNumeros() {
        return numeros;
    }

    @Init
    public void init(@BindingParam("lieu") LieuPredefini lieu, @BindingParam("adresse") Adresse _adresse, @BindingParam("idFiche") Long _idFiche) {
        lieuPredefini = lieu;
        adresse = new Adresse();

        if (lieu != null && lieu.getAdresse() != null) {
            adresse.setQuartier(lieu.getAdresse().getQuartier());
            adresse.setNomVoie(lieu.getAdresse().getNomVoie());
            adresse.setNumeroVoie(lieu.getAdresse().getNumeroVoie());
        } else {
            adresse = _adresse;
            idFiche = _idFiche;
        }

        initAdresseFields();
    }

    protected void initAdresseFields() {

        if (adresse == null) {
            villes = adresseService.getAllVilles();
            selectedVille = villes.get(2);
            refreshAddressVille();
            nomVoies = new ContainsSimpleListModel(adresseService.getAllNomVoies(), NomVoie.class, "libelle");
            return;
        }

        Quartier q = adresse.getQuartier();
        Secteur s = q.getSecteur();
        NomVoie nv = adresse.getNomVoie();
        Ville v = s.getVille();

        villes = adresseService.getAllVilles();
        selectedVille = v;

        secteurs = new ContainsSimpleListModel(adresseService.getAllSecteursOfVille(selectedVille), Secteur.class, "libelle");
        selectedSecteur = s;

        quartiers = new ContainsSimpleListModel(adresseService.getAllQuartiersOfNomVoieAndNumero(nv, numero), Quartier.class, "libelle");
        selectedQuartier = q;

        nomVoies = new ContainsSimpleListModel(adresseService.getAllNomVoiesOfQuartier(selectedQuartier), NomVoie.class, "libelle");
        selectedNomVoie = nv;

        numeros = adresseService.getAllNumeroOfNomVoie(selectedNomVoie);
        numero = adresse.getNumeroVoie();
    }

    @Command
    @NotifyChange({"selectedSecteur", "secteurs", "selectedQuartier", "quartiers"})
    public void refreshAddressVille() {

        selectedSecteur = null;
        selectedQuartier = null;

        if (selectedVille == null) {
            secteurs = null;
            quartiers = null;
            return;
        }

        secteurs = new ContainsSimpleListModel(adresseService.getAllSecteursOfVille(selectedVille), Secteur.class, "libelle");
    }

    @Command
    @NotifyChange({"selectedQuartier", "quartiers"})
    public void refreshAddressSecteur() {

        selectedQuartier = null;

        if (selectedSecteur == null) {
            quartiers = null;
            return;
        }

        quartiers = new ContainsSimpleListModel(adresseService.getAllQuartiersOfSecteur(selectedSecteur), Quartier.class, "libelle");
    }

    @Command
    @NotifyChange({"nomVoies", "numeros", "secteurs", "selectedSecteur", "selectedVille"})
    public void refreshAddressQuartier() {

        if (selectedQuartier == null) {
            nomVoies = new ContainsSimpleListModel(adresseService.getAllNomVoies(), NomVoie.class, "libelle");
            secteurs = new ContainsSimpleListModel(adresseService.getAllSecteursOfVille(selectedVille), Secteur.class, "libelle");
            return;
        }

        nomVoies = new ContainsSimpleListModel(adresseService.getAllNomVoiesOfQuartier(selectedQuartier), NomVoie.class, "libelle");
        numeros = adresseService.getAllNumeroOfNomVoieAndQuartier(selectedNomVoie, selectedQuartier);

        if (selectedSecteur == null) {
            secteurs = new ContainsSimpleListModel(Arrays.asList(selectedQuartier.getSecteur()), Secteur.class, "libelle");
            selectedSecteur = selectedQuartier.getSecteur();
            selectedVille = selectedSecteur.getVille();
        }

    }

    @Command
    @NotifyChange({"numeros", "quartiers"})
    public void refreshAddressNomVoie() {

        if (selectedNomVoie == null) {
            numeros = null;
            quartiers = new ContainsSimpleListModel(adresseService.getAllQuartiersOfSecteur(selectedSecteur), Quartier.class, "libelle");
            return;
        }

        if (selectedQuartier != null)
            numeros = adresseService.getAllNumeroOfNomVoieAndQuartier(selectedNomVoie, selectedQuartier);
        else
            numeros = adresseService.getAllNumeroOfNomVoie(selectedNomVoie);

        quartiers = new ContainsSimpleListModel(adresseService.getAllQuartiersOfNomVoieAndNumero(selectedNomVoie, numero), Quartier.class, "libelle");
    }

    @Command
    public void save(@BindingParam("win") Window x) {

        Adresse address = adresseService.getAdresseFromParams(selectedNomVoie, numero, selectedQuartier);
        if (address == null) {
            Messagebox.show("Cette adresse n'existe pas. Veuillez en sélectionner une existante.",
                    "Erreur dans l'adresse", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        x.detach();

        if (idFiche == null) {
            Map<String, Object> args = new HashMap<>();
            lieuPredefini.setAdresse(address);
            args.put("lieu", lieuPredefini);
            BindUtils.postGlobalCommand(null, null, "updateLieuPredefini", args);
        } else {
            Map<String, Object> args2 = new HashMap<>();
            args2.put("adresse", address);
            args2.put("idFiche", idFiche);
            BindUtils.postGlobalCommand(null, null, "updateAdresse", args2);
        }
    }
}
