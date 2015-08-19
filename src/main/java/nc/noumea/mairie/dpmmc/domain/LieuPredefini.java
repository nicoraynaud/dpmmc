package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "LIEUX_PREDEFINIS")
public class LieuPredefini extends ReferenceList {

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="ID_ADRESSE")
    private Adresse adresse;

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
}
