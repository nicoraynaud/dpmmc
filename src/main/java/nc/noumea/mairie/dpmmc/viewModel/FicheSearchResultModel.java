package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Fait;
import nc.noumea.mairie.dpmmc.domain.Fiche;
import nc.noumea.mairie.dpmmc.domain.Historique;

import java.util.Date;

public class FicheSearchResultModel {

    private Long idFiche;
    private String reference;
    private Date dateSaisie;
    private String agentSaisie;
    private String natureFaits;
    private String quartier;
    private String categorieSaisine;

    private boolean canEdit;
    private boolean recidive;

    public FicheSearchResultModel(Fiche fiche) {
        this.idFiche = fiche.getId();
        this.reference = fiche.getReference();
        this.quartier = fiche.getAdresse() == null ? "" :  fiche.getAdresse().getQuartier().getLibelle();
        this.categorieSaisine = fiche.getCategorieSaisine().getLibelleLong();
    }

    public FicheSearchResultModel(Historique histo) {
        this.idFiche = histo.getFiche().getId();
        this.reference = histo.getFiche().getReference();
        this.dateSaisie = histo.getDateAction();
        this.agentSaisie = String.format("%s %s", histo.getUtilisateur().getAgent().getNom(), histo.getUtilisateur().getAgent().getPrenom());
        this.quartier = histo.getFiche().getAdresse() == null ? "" :  histo.getFiche().getAdresse().getQuartier().getLibelle();

        this.natureFaits = "";
        for (Fait f : histo.getFiche().getFaits()) {
            this.natureFaits = String.format("%s %s", this.natureFaits, f.getNature().getLibelleCourt());
        }
    }


    public FicheSearchResultModel(Long idFiche, String reference, Date dateSaisie, String agentSaisie, String natureFaits, String quartier, boolean canEdit) {
        this.idFiche = idFiche;
        this.reference = reference;
        this.dateSaisie = dateSaisie;
        this.agentSaisie = agentSaisie;
        this.natureFaits = natureFaits;
        this.quartier = quartier;
        this.canEdit = canEdit;
        this.recidive = false;
    }

    public FicheSearchResultModel() {

    }

    public Long getIdFiche() {
        return idFiche;
    }

    public void setIdFiche(Long idFiche) {
        this.idFiche = idFiche;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(Date dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public String getAgentSaisie() {
        return agentSaisie;
    }

    public void setAgentSaisie(String agentSaisie) {
        this.agentSaisie = agentSaisie;
    }

    public String getNatureFaits() {
        return natureFaits;
    }

    public void setNatureFaits(String natureFaits) {
        this.natureFaits = natureFaits;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getCategorieSaisine() {
        return categorieSaisine;
    }

    public void setCategorieSaisine(String categorieSaisine) {
        this.categorieSaisine = categorieSaisine;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isRecidive() {
        return recidive;
    }

    public void setRecidive(boolean recidive) {
        this.recidive = recidive;
    }
}
