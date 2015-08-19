package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SECTEURS")
@NamedQueries({
        @NamedQuery(name = "getAllSecteursOfVille", query = "select s from Secteur s where s.ville.id = :idVille"),
        @NamedQuery(name = "getSecteurFromLabel", query = "select s from Secteur s where s.libelle like :label")
})
public class Secteur {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LIBELLE")
    private String libelle;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ID_VILLE")
    private Ville ville;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @OrderBy("id asc")
    @OneToMany(mappedBy = "secteur")
    private Set<Quartier> quartiers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville secteur) {
        this.ville = secteur;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Set<Quartier> getQuartiers() {
        return quartiers;
    }

    public void setQuartiers(Set<Quartier> quartiers) {
        this.quartiers = quartiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Secteur)) return false;

        Secteur secteur = (Secteur) o;

        if (id != null ? !id.equals(secteur.id) : secteur.id != null) return false;
        if (libelle != null ? !libelle.equals(secteur.libelle) : secteur.libelle != null) return false;
        if (version != null ? !version.equals(secteur.version) : secteur.version != null) return false;
        if (ville != null ? !ville.equals(secteur.ville) : secteur.ville != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (libelle != null ? libelle.hashCode() : 0);
        result = 31 * result + (ville != null ? ville.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
