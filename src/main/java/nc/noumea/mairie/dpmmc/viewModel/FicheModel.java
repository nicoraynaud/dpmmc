package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Fiche;

import java.util.Date;

public class FicheModel {

    private Fiche fiche;
    private boolean canEdit;
    private boolean recidive;

    private String creator;
    private Date createDate;
    private String modifier;
    private Date modifiedDate;
    private String motif;

    private boolean canCloture;
    private boolean canDecloture;
    private boolean canDelete;

    public FicheModel(Fiche fiche, boolean canEdit, boolean recidive) {
        this.fiche = fiche;
        this.canEdit = canEdit;
        this.recidive = recidive;
    }

    public FicheModel(Fiche fiche) {
        this.fiche = fiche;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public boolean isCanCloture() {
        return canCloture;
    }

    public void setCanCloture(boolean canCloture) {
        this.canCloture = canCloture;
    }

    public boolean isCanDecloture() {
        return canDecloture;
    }

    public void setCanDecloture(boolean canDecloture) {
        this.canDecloture = canDecloture;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
