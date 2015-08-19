package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "PERSONNES")
public class Personne {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_CATEGORIE")
    private CategoriePersonne categorie;

    @Column(name = "NOMBRE")
    private Integer nombre;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "NOM")
    private String nom;

    @ManyToOne
    @JoinColumn(name="ID_FICHE")
    private Fiche fiche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoriePersonne getCategorie() {
        return categorie;
    }

    public void setCategorie(CategoriePersonne categorie) {
        this.categorie = categorie;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }
}
