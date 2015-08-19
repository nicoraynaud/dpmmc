package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@MappedSuperclass
public abstract class ReferenceList {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="LIBELLE_COURT")
    protected String libelleCourt;

    @Column(name="LIBELLE_LONG", nullable = false)
    protected String libelleLong;

    @Column(name="ARCHIVEE")
    protected boolean archivee;

    @Column(name="PAR_DEFAUT")
    protected boolean parDefaut;

    @Column(name="ORDRE")
    protected Integer ordre;

    @Column(name="STATS")
    protected boolean statistiques;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public boolean isArchivee() {
        return archivee;
    }

    public void setArchivee(boolean archivee) {
        this.archivee = archivee;
    }

    public boolean isParDefaut() {
        return parDefaut;
    }

    public void setParDefaut(boolean parDefaut) {
        this.parDefaut = parDefaut;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public boolean isStatistiques() {
        return statistiques;
    }

    public void setStatistiques(boolean statistiques) {
        this.statistiques = statistiques;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceList)) return false;

        ReferenceList that = (ReferenceList) o;

        if (archivee != that.archivee) return false;
        if (parDefaut != that.parDefaut) return false;
        if (statistiques != that.statistiques) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (libelleCourt != null ? !libelleCourt.equals(that.libelleCourt) : that.libelleCourt != null) return false;
        if (libelleLong != null ? !libelleLong.equals(that.libelleLong) : that.libelleLong != null) return false;
        if (ordre != null ? !ordre.equals(that.ordre) : that.ordre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (libelleCourt != null ? libelleCourt.hashCode() : 0);
        result = 31 * result + (libelleLong != null ? libelleLong.hashCode() : 0);
        result = 31 * result + (archivee ? 1 : 0);
        result = 31 * result + (parDefaut ? 1 : 0);
        result = 31 * result + (ordre != null ? ordre.hashCode() : 0);
        result = 31 * result + (statistiques ? 1 : 0);
        return result;
    }
}
