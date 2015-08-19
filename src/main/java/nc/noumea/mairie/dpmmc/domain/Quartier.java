package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "QUARTIERS")
@NamedQueries({
        @NamedQuery(name = "getAllQuartiersOfSecteur", query = "select q from Quartier q where q.secteur.id = :idSecteur")
})
public class Quartier {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "LIBELLE")
    private String libelle;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ID_SECTEUR")
    private Secteur secteur;

    @Version
    @Column(name = "VERSION")
    private Integer version;

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

    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quartier)) return false;

        Quartier quartier = (Quartier) o;

        if (id != null ? !id.equals(quartier.id) : quartier.id != null) return false;
        if (libelle != null ? !libelle.equals(quartier.libelle) : quartier.libelle != null) return false;
        if (secteur != null ? !secteur.equals(quartier.secteur) : quartier.secteur != null) return false;
        if (version != null ? !version.equals(quartier.version) : quartier.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (libelle != null ? libelle.hashCode() : 0);
        result = 31 * result + (secteur != null ? secteur.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}

