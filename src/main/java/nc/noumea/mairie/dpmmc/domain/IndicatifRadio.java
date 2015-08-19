package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;

@Entity
@Table(name = "INDICATIFS_RADIOS")
@NamedQuery(name = "getIndicatifRadiosFromBrigade", query = "select i from IndicatifRadio i where i.brigade.id = :idBrigade and i.archivee is false")
public class IndicatifRadio extends ReferenceList {

    @ManyToOne
    @JoinColumn(name="ID_BRIGADE")
    private Brigade brigade;

    public Brigade getBrigade() {
        return brigade;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }
}
