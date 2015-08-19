package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.AgentDao;
import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.Brigade;
import nc.noumea.mairie.dpmmc.services.interfaces.IAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService implements IAgentService {

    @Autowired
    private AgentDao agentDao;

    @Override
    public List<Agent> getAgents() {
        return agentDao.getAgents();
    }

    @Override
    public List<Agent> getAgentsFromBrigade(Brigade brigade) {
        return agentDao.getAgentsFromBrigade(brigade);
    }

}
