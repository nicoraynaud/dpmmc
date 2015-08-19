package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "GEOLOCALISATIONS_VP")
@NamedQueries({
        @NamedQuery(name = "doesGeolocAlreadyExists", query = "select case when (count(d) > 0)  then true else false end from GeolocalisationVP d where d.donneesGeoloc = :donneesGeoloc"),
        @NamedQuery(name = "getGeolocVpByIdForLock", query = "select gvp from GeolocalisationVP gvp where gvp.id = :id")
})
public class GeolocalisationVP {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="DATE_HEURE")
    private Date dateHeure;

    @ManyToOne
    @JoinColumn(name="ID_INDICATIF_RADIO")
    private IndicatifRadio indicatifRadio;

    @ManyToOne
    @JoinColumn(name="ID_VEHICULE")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name="ID_ADRESSE")
    private Adresse adresse;

    @OrderBy("id asc")
    @ManyToMany
    @JoinTable(name="EQUIPAGES_GEOLOCALISATIONS_VP",
            joinColumns={@JoinColumn(name="ID_GEOLOCALISATION_VP")},
            inverseJoinColumns={@JoinColumn(name="ID_AGENT")})
    private Set<Agent> agents = new LinkedHashSet<Agent>();

    @OrderBy("id asc")
    @OneToMany(mappedBy="geolocalisationVP", cascade = CascadeType.ALL)
    private Set<ErreurGeolocalisationVP> erreursGeocalisationVP = new LinkedHashSet<>();

    @Column(name="AFFICHEE")
    private boolean affichee;

    @Column(name="DONNEES_GEOLOC")
    private String donneesGeoloc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    public IndicatifRadio getIndicatifRadio() {
        return indicatifRadio;
    }

    public void setIndicatifRadio(IndicatifRadio indicatifRadio) {
        this.indicatifRadio = indicatifRadio;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Set<ErreurGeolocalisationVP> getErreursGeocalisationVP() {
        return erreursGeocalisationVP;
    }

    public void setErreursGeocalisationVP(Set<ErreurGeolocalisationVP> erreursGeocalisationVP) {
        this.erreursGeocalisationVP = erreursGeocalisationVP;
    }

    public boolean isAffichee() {
        return affichee;
    }

    public void setAffichee(boolean affichee) {
        this.affichee = affichee;
    }

    public String getDonneesGeoloc() {
        return donneesGeoloc;
    }

    public void setDonneesGeoloc(String donneesGeoloc) {
        this.donneesGeoloc = donneesGeoloc;
    }
}
