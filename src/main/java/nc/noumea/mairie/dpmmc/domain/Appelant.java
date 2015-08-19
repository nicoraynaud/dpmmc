package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "APPELANTS")
public class Appelant {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REQUERANT")
    private String requerant;

    @Column(name = "NUMERO")
    private String numero;

    @ManyToOne
    @JoinColumn(name="ID_FICHE")
    private Fiche fiche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequerant() {
        return requerant;
    }

    public void setRequerant(String requerant) {
        this.requerant = requerant;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }
}
