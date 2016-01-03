package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "FAITS")
public class Fait {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_NATURE")
    private NatureFait nature;

    @Column(name = "NOMBRE")
    private Integer nombre;

    @ManyToOne
    @JoinColumn(name="ID_FICHE")
    private Fiche fiche;

    @Column(name="VIDEO")
    private boolean video;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NatureFait getNature() {
        return nature;
    }

    public void setNature(NatureFait nature) {
        this.nature = nature;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
