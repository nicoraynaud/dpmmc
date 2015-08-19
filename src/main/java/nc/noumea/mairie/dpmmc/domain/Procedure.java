package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "PROCEDURES")
public class Procedure {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_CATEGORIE")
    private CategorieProcedure categorie;

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

    public CategorieProcedure getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieProcedure categorie) {
        this.categorie = categorie;
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
