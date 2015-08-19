package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.Profil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProfilDao extends GenericDao {

    @Transactional
    public List<Profil> getAllProfils() {
        TypedQuery<Profil> query = em.createQuery("from Profil order by nom asc", Profil.class);

        return query.getResultList();
    }

}
