package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "ADRESSES")
@NamedQueries({
        @NamedQuery(name = "getAllNumerosOfNomVoie", query = "select distinct a.numeroVoie from Adresse a where a.nomVoie.id = :idNomVoie order by a.numeroVoie"),
        @NamedQuery(name = "getAllNumerosOfNomVoieAndQuartier", query="select distinct a.numeroVoie from Adresse a where a.nomVoie.id = :idNomVoie and a.quartier.id = :idQuarter order by a.numeroVoie"),
        @NamedQuery(name = "getAdresseFromParams", query = "select a from Adresse a where a.nomVoie.id = :idNomVoie and a.numeroVoie = :numero and a.quartier.id = :idQuartier"),
        @NamedQuery(name = "getAllQuartiersOfNomVoieAndNumero", query = "select distinct a.quartier from Adresse a where a.nomVoie.id = :idNomVoie and a.numeroVoie like :numero"),
        @NamedQuery(name = "getAllQuartiersOfNomVoie", query = "select distinct a.quartier from Adresse a where a.nomVoie.id = :idNomVoie"),
        @NamedQuery(name = "getAdresseOfLieuPredefini", query = "select l.adresse from LieuPredefini l where l.id = :idLieu")
})
public class Adresse {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name="NUMERO_VOIE")
    private String numeroVoie;

    @ManyToOne
    @JoinColumn(name="ID_NOM_VOIE")
    private NomVoie nomVoie;

    @ManyToOne
    @JoinColumn(name="ID_QUARTIER")
    private Quartier quartier;

    @Column(name="X")
    private Float coordonneeX;

    @Column(name="Y")
    private Float coordonneeY;

    @Column(name="DONNEE_BRUTE")
    private String donneeBrute;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVoie() {
        return numeroVoie;
    }

    public void setNumeroVoie(String numeroVoie) {
        this.numeroVoie = numeroVoie;
    }

    public NomVoie getNomVoie() {
        return nomVoie;
    }

    public void setNomVoie(NomVoie nomVoie) {
        this.nomVoie = nomVoie;
    }

    public Quartier getQuartier() {
        return quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Float getCoordonneeX() {
        return coordonneeX;
    }

    public void setCoordonneeX(Float coordonneeX) {
        this.coordonneeX = coordonneeX;
    }

    public Float getCoordonneeY() {
        return coordonneeY;
    }

    public void setCoordonneeY(Float coordonneeY) {
        this.coordonneeY = coordonneeY;
    }

    public String getDonneeBrute() {
        return donneeBrute;
    }

    public void setDonneeBrute(String donneeBrute) {
        this.donneeBrute = donneeBrute;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
