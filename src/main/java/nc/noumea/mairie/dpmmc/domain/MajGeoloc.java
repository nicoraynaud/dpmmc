package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="MAJ_GEOLOC")
public class MajGeoloc {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="VALEUR")
    private Date dateDerniereMaj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDerniereMaj() {
        return dateDerniereMaj;
    }

    public void setDateDerniereMaj(Date dateDerniereMaj) {
        this.dateDerniereMaj = dateDerniereMaj;
    }
}
