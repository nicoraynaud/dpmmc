package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "PROFILS")
public class Profil {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOM")
    private String nom;

    @Column(name = "DROIT_ADMINISTRATION")
    private boolean droitAdministration;

    @Column(name = "DROIT_CREATION_FICHE")
    private boolean droitCreationFiche;

    @Column(name = "DROIT_MODIF_FICHE_N_BRIGADES")
    private boolean droitModificationFicheToutesBrigades;

    @Column(name = "DROIT_MODIF_FICHE_1_BRIGADE")
    private boolean droitModificationFicheLaBrigade;

    @Column(name = "DROIT_CLOTURE_FICHE")
    private boolean droitClotureFiche;

    @Column(name = "DROIT_DECLOTURE_FICHE")
    private boolean droitDeclotureFiche;

    @Column(name = "DROIT_SUPPRESSION_FICHE")
    private boolean droitSuppressionFiche;

    @Column(name = "DROIT_RECHERCHE_FICHE")
    private boolean droitRechercheFiche;

    @Column(name = "DROIT_STATISTIQUES")
    private boolean droitStatistiques;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isDroitAdministration() {
        return droitAdministration;
    }

    public void setDroitAdministration(boolean droitAdministration) {
        this.droitAdministration = droitAdministration;
    }

    public boolean isDroitCreationFiche() {
        return droitCreationFiche;
    }

    public void setDroitCreationFiche(boolean droitCreationFiche) {
        this.droitCreationFiche = droitCreationFiche;
    }

    public boolean isDroitModificationFicheToutesBrigades() {
        return droitModificationFicheToutesBrigades;
    }

    public void setDroitModificationFicheToutesBrigades(boolean droitModificationFicheToutesBrigades) {
        this.droitModificationFicheToutesBrigades = droitModificationFicheToutesBrigades;
    }

    public boolean isDroitModificationFicheLaBrigade() {
        return droitModificationFicheLaBrigade;
    }

    public void setDroitModificationFicheLaBrigade(boolean droitModificationFicheLaBrigade) {
        this.droitModificationFicheLaBrigade = droitModificationFicheLaBrigade;
    }

    public boolean isDroitClotureFiche() {
        return droitClotureFiche;
    }

    public void setDroitClotureFiche(boolean droitClotureFiche) {
        this.droitClotureFiche = droitClotureFiche;
    }

    public boolean isDroitDeclotureFiche() {
        return droitDeclotureFiche;
    }

    public void setDroitDeclotureFiche(boolean droitDeclotureFiche) {
        this.droitDeclotureFiche = droitDeclotureFiche;
    }

    public boolean isDroitSuppressionFiche() {
        return droitSuppressionFiche;
    }

    public void setDroitSuppressionFiche(boolean droitSuppressionFiche) {
        this.droitSuppressionFiche = droitSuppressionFiche;
    }

    public boolean isDroitRechercheFiche() {
        return droitRechercheFiche;
    }

    public void setDroitRechercheFiche(boolean droitRechercheFiche) {
        this.droitRechercheFiche = droitRechercheFiche;
    }

    public boolean isDroitStatistiques() {
        return droitStatistiques;
    }

    public void setDroitStatistiques(boolean droitStatistiques) {
        this.droitStatistiques = droitStatistiques;
    }
}
