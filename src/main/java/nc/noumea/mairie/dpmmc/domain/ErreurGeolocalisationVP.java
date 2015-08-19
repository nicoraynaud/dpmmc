package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "ERREURS_GEOLOCALISATIONS_VP")
public class ErreurGeolocalisationVP {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LIBELLE")
    private String libelle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ID_GEOLOCALISATION_VP")
    private GeolocalisationVP geolocalisationVP;

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

    public GeolocalisationVP getGeolocalisationVP() {
        return geolocalisationVP;
    }

    public void setGeolocalisationVP(GeolocalisationVP geolocalisationVP) {
        this.geolocalisationVP = geolocalisationVP;
    }
}
