package nc.noumea.mairie.dpmmc.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "AGENTS")
@NamedQuery(name = "getAgentsFromBrigade", query = "select distinct a from Agent a left join a.rattachements as r where r.brigade.id = :idBrigade and r.debut <= :date and (r.fin >= :date or r.fin is null)")
public class Agent {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NOM")
    private String nom;

    @Column(name="PRENOM")
    private String prenom;

    @Column(name="MATRICULE")
    private Integer matricule;

    @OrderBy("id desc")
    @OneToMany(mappedBy="agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rattachement> rattachements = new HashSet<>();

    @OneToOne(mappedBy = "agent")
    private Utilisateur utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getMatricule() {
        return matricule;
    }

    public void setMatricule(Integer matricule) {
        this.matricule = matricule;
    }

    public Set<Rattachement> getRattachements() {
        return rattachements;
    }

    public void setRattachements(Set<Rattachement> rattachements) {
        this.rattachements = rattachements;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
