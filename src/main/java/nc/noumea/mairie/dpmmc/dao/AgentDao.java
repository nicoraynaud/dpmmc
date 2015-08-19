package nc.noumea.mairie.dpmmc.dao;

import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.domain.IndicatifRadio;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class AgentDao extends GenericDao {

    public List<Agent> getAgents() {
        return getAll(Agent.class);
    }

    public List<Agent> getAgentsFromBrigade(Brigade brigade) {
        TypedQuery<Agent> query = em.createNamedQuery("getAgentsFromBrigade", Agent.class);
        query.setParameter("date", new Date());
        query.setParameter("idBrigade", brigade.getId());
        return query.getResultList();
    }
}
