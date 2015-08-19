package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RATTACHEMENTS")
public class Rattachement {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_BRIGADE")
    private Brigade brigade;

    @ManyToOne
    @JoinColumn(name="ID_AGENT")
    private Agent agent;

    @Column(name = "BRIGADE_HABITUELLE")
    private boolean brigadeHabituelle;

    @Column(name = "DEBUT")
    private Date debut;

    @Column(name = "FIN")
    private Date fin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brigade getBrigade() {
        return brigade;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public boolean isBrigadeHabituelle() {
        return brigadeHabituelle;
    }

    public void setBrigadeHabituelle(boolean brigadeHabituelle) {
        this.brigadeHabituelle = brigadeHabituelle;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }
}
