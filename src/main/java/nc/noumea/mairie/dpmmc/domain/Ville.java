package nc.noumea.mairie.dpmmc.domain;


import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "VILLES")
@NamedQuery(name = "getVilleFromLabel", query = "select v from Ville v where v.libelle like :label")
public class Ville {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LIBELLE")
    private String libelle;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @OrderBy("id asc")
    @OneToMany(mappedBy = "ville")
    private Set<Secteur> secteurs;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Set<Secteur> getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(Set<Secteur> secteurs) {
        this.secteurs = secteurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;

        Ville ville = (Ville) o;

        if (id != null ? !id.equals(ville.id) : ville.id != null) return false;
        if (libelle != null ? !libelle.equals(ville.libelle) : ville.libelle != null) return false;
        if (version != null ? !version.equals(ville.version) : ville.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (libelle != null ? libelle.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
