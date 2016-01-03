package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "FICHES")
@NamedQueries({
        @NamedQuery(name = "getDashboardFiches", query = "select f from Fiche f join f.patrouilles as p join p.indicatif as i join i.brigade as b where f.statut = :statut and b in (:brigades)"),
        @NamedQuery(name = "getFicheById", query = "select f from Fiche f left join fetch f.historiques left join fetch f.procedures left join fetch f.personnes left join fetch f.faits left join fetch f.appelants left join fetch f.patrouilles where f.id = :idFiche")
})
public class Fiche {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "STATUT")
    @Enumerated(EnumType.STRING)
    private StatutEnum statut;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_CATEGORIE_SAISINE")
    private CategorieSaisine categorieSaisine;

    @ManyToOne
    @JoinColumn(name="ID_CONTACT_INTERPOLICE")
    private ContactInterpolice contactInterpolice;

    @Column(name = "SITUATION_INITIALE")
    private String situationInitiale;

    @Column(name = "SITUATION_INITIALE_TITRE")
    private String situationInitialeTitre;

    @Column(name = "SUITE_DONNEE")
    private String suiteDonnee;

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appelant> appelants = new LinkedHashSet<>();

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fait> faits = new LinkedHashSet<Fait>();

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Personne> personnes = new LinkedHashSet<Personne>();

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Procedure> procedures = new LinkedHashSet<Procedure>();

    @ManyToOne
    @JoinColumn(name="ID_AGENT_INTERVENTION")
    private Agent agentIntervention;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_ADRESSE")
    private Adresse adresse;

    @Column(name = "COMPLEMENT_ADRESSE")
    private String complementAdresse;

    @ManyToOne
    @JoinColumn(name="ID_LIEU_PREDEFINI")
    private LieuPredefini lieuPredefini;

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Patrouille> patrouilles = new LinkedHashSet<Patrouille>();

    @Column(name="GDH_APPEL")
    private Date gdhAppel;

    @Column(name="GDH_ARRIVEE")
    private Date gdhArrivee;

    @OrderBy("id asc")
    @OneToMany(mappedBy="fiche", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Historique> historiques = new LinkedHashSet<Historique>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StatutEnum getStatut() {
        return statut;
    }

    public void setStatut(StatutEnum statut) {
        this.statut = statut;
    }

    public CategorieSaisine getCategorieSaisine() {
        return categorieSaisine;
    }

    public void setCategorieSaisine(CategorieSaisine categorieSaisine) {
        this.categorieSaisine = categorieSaisine;
    }

    public ContactInterpolice getContactInterpolice() {
        return contactInterpolice;
    }

    public void setContactInterpolice(ContactInterpolice contactInterpolice) {
        this.contactInterpolice = contactInterpolice;
    }

    public String getSituationInitiale() {
        return situationInitiale;
    }

    public void setSituationInitiale(String situationInitiale) {
        this.situationInitiale = situationInitiale;
    }

    public String getSituationInitialeTitre() {
        return situationInitialeTitre;
    }

    public void setSituationInitialeTitre(String situationInitialeTitre) {
        this.situationInitialeTitre = situationInitialeTitre;
    }

    public String getSuiteDonnee() {
        return suiteDonnee;
    }

    public void setSuiteDonnee(String suiteDonnee) {
        this.suiteDonnee = suiteDonnee;
    }

    public Set<Appelant> getAppelants() {
        return appelants;
    }

    public void setAppelants(Set<Appelant> appelants) {
        this.appelants = appelants;
    }

    public Set<Fait> getFaits() {
        return faits;
    }

    public void setFaits(Set<Fait> faits) {
        this.faits = faits;
    }

    public Set<Personne> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(Set<Personne> personnes) {
        this.personnes = personnes;
    }

    public Set<Procedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(Set<Procedure> procedures) {
        this.procedures = procedures;
    }

    public Agent getAgentIntervention() {
        return agentIntervention;
    }

    public void setAgentIntervention(Agent agentIntervention) {
        this.agentIntervention = agentIntervention;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public LieuPredefini getLieuPredefini() {
        return lieuPredefini;
    }

    public void setLieuPredefini(LieuPredefini lieuPredefini) {
        this.lieuPredefini = lieuPredefini;
    }

    public Set<Patrouille> getPatrouilles() {
        return patrouilles;
    }

    public void setPatrouilles(Set<Patrouille> patrouilles) {
        this.patrouilles = patrouilles;
    }

    public Date getGdhAppel() {
        return gdhAppel;
    }

    public void setGdhAppel(Date gdhAppel) {
        this.gdhAppel = gdhAppel;
    }

    public Date getGdhArrivee() {
        return gdhArrivee;
    }

    public void setGdhArrivee(Date gdhArrivee) {
        this.gdhArrivee = gdhArrivee;
    }

    public Set<Historique> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(Set<Historique> historiques) {
        this.historiques = historiques;
    }

    public Personne addPersonne(Personne p) {
        p.setFiche(this);
        getPersonnes().add(p);

        return p;
    }

    public Fait addFait(Fait f) {
        f.setFiche(this);
        getFaits().add(f);

        return f;
    }

    public Procedure addProcedure(Procedure p) {
        p.setFiche(this);
        getProcedures().add(p);

        return p;
    }

    public Appelant addAppelant(Appelant a) {
        a.setFiche(this);
        getAppelants().add(a);

        return a;
    }

    public Patrouille addPatrouille(Patrouille p) {
        p.setFiche(this);
        getPatrouilles().add(p);

        return p;
    }

    public Historique addHistorique(Historique h) {
        h.setFiche(this);
        getHistoriques().add(h);

        return h;
    }
}
