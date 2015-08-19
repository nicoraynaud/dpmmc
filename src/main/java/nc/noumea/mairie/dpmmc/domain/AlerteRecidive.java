package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "ALERTES_RECIDIVES")
public class AlerteRecidive {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_NATURE")
    private NatureFait natureFait;

    /** Ecart en nombre de jours entre 2 récidives */
    @Column(name = "DELAI")
    private Integer delai;

    /** Nombre de récidives ayant la même nature de fait levant une alerte */
    @Column(name = "FREQUENCE")
    private Integer frequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NatureFait getNatureFait() {
        return natureFait;
    }

    public void setNatureFait(NatureFait natureFait) {
        this.natureFait = natureFait;
    }

    public Integer getDelai() {
        return delai;
    }

    public void setDelai(Integer delai) {
        this.delai = delai;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public void setFrequence(Integer frequence) {
        this.frequence = frequence;
    }
}
