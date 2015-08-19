package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HISTORIQUES")
public class Historique {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_FICHE")
    private Fiche fiche;

    @ManyToOne
    @JoinColumn(name="ID_UTILISATEUR")
    private Utilisateur utilisateur;

    @Column(name="DATE_ACTION")
    private Date dateAction;

    @Enumerated(EnumType.STRING)
    private ActionEnum action;

    @Column(name="MOTIF_ACTION")
    private String motifAction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public String getMotifAction() {
        return motifAction;
    }

    public void setMotifAction(String motifAction) {
        this.motifAction = motifAction;
    }
}
