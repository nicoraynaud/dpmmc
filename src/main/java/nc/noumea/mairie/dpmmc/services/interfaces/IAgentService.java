package nc.noumea.mairie.dpmmc.services.interfaces;

import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.Brigade;

import java.util.List;

public interface IAgentService {

    List<Agent> getAgents();

    List<Agent> getAgentsFromBrigade(Brigade brigade);
}
