package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "NOM_VOIES")
@NamedQuery(name = "getAllNomVoiesOfQuartier", query = "select distinct v from Adresse a left join a.nomVoie as v where a.quartier.id = :idQuartier order by v.libelle")
public class NomVoie {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "LIBELLE")
    private String libelle;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NomVoie)) return false;

        NomVoie nomVoie = (NomVoie) o;

        if (id != null ? !id.equals(nomVoie.id) : nomVoie.id != null) return false;
        if (libelle != null ? !libelle.equals(nomVoie.libelle) : nomVoie.libelle != null) return false;
        if (version != null ? !version.equals(nomVoie.version) : nomVoie.version != null) return false;

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

