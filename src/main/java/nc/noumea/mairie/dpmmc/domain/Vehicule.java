package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "VEHICULES")
public class Vehicule {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="IMMATRICULATION")
    private String immatriculation;

    @Column(name="TYPE")
    private String type;

    @Column(name="ARCHIVEE")
    private boolean archivee;

    @Column(name="PAR_DEFAUT")
    private boolean parDefaut;

    @Column(name="ORDRE")
    private Integer ordre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
