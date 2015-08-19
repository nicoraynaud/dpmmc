package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRE_FICHES")
public class PreFiche {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE_CREATION")
    private Date dateCreation;

    @Column(name = "NUMERO")
    private Long numero;

    @ManyToOne
    @JoinColumn(name="ID_UTILISATEUR")
    private Utilisateur utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
