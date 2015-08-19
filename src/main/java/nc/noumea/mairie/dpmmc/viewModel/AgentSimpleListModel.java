package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Agent;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zul.SimpleListModel;

import java.util.List;

public class AgentSimpleListModel extends SimpleListModel {

    public AgentSimpleListModel(Object[] data, boolean live) {
        super(data, live);
    }

    public AgentSimpleListModel(Object[] data) {
        super(data);
    }

    public AgentSimpleListModel(List data) {
        super(data);
    }

    @Override
    public boolean inSubModel(Object key, Object value) {

        String searchString = (String)key;
        Agent agent = (Agent) value;

        return StringUtils.containsIgnoreCase(agent.getNom(), searchString)
                || StringUtils.containsIgnoreCase(agent.getPrenom(), searchString)
                || StringUtils.containsIgnoreCase(Integer.toString(agent.getMatricule()), searchString);
    }
}
