package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "UTILISATEURS")
@NamedQueries({
        @NamedQuery(name = "getAllUtilisateurs", query="select distinct u from Utilisateur u left join fetch u.agent as a left join fetch a.rattachements order by u.identifiant asc"),
        @NamedQuery(name = "getUtilisateurByIdentifiant", query="from Utilisateur where identifiant = :identifiant"),
        @NamedQuery(name = "getBrigadesOfUtilisateur", query = "select distinct r.brigade from Rattachement r join r.agent as a join a.utilisateur as u where u.identifiant = :identifiant and r.debut <= :dateActuelle and (r.fin = null or r.fin >= :dateActuelle)")
})
public class Utilisateur {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IDENTIFIANT")
    private String identifiant;

    @Column(name = "ACTIF")
    private boolean actif;

    @ManyToOne
    @JoinColumn(name="ID_PROFIL")
    private Profil profil;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ID_AGENT")
    private Agent agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
