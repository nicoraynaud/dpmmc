package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NATURES_FAIT")
public class NatureFait extends ReferenceList {

    @ManyToOne
    @JoinColumn(name="ID_CATEGORIE_FAIT")
    private CategorieFait categorieFait;

    public CategorieFait getCategorieFait() {
        return categorieFait;
    }

    public void setCategorieFait(CategorieFait categorieFait) {
        this.categorieFait = categorieFait;
    }
}
