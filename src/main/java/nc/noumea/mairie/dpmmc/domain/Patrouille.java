package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PATROUILLES")
public class Patrouille {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_VEHICULE")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name="ID_INDICATIF_RADIO")
    private IndicatifRadio indicatif;

    @OrderBy("id asc")
    @ManyToMany
    @JoinTable(name="EQUIPAGES",
            joinColumns={@JoinColumn(name="ID_PATROUILLE")},
            inverseJoinColumns={@JoinColumn(name="ID_AGENT")})
    private Set<Agent> agents = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name="ID_FICHE")
    private Fiche fiche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public IndicatifRadio getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(IndicatifRadio indicatif) {
        this.indicatif = indicatif;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Fiche getFiche() {
        return fiche;
    }

    public void setFiche(Fiche fiche) {
        this.fiche = fiche;
    }

    @Transient
    public String getLabel() {

        if (indicatif == null)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append(indicatif.getLibelleLong());
        sb.append(" :");

        boolean first = true;
        for (Agent a : agents) {
            if (!first) {
                sb.append(",");
            }
            sb.append(" ");
            sb.append(String.format(" %s %s", a.getNom(), a.getPrenom()));
            first = false;
        }

        return sb.toString();
    }

}
