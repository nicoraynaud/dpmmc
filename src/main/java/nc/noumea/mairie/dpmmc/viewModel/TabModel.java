package nc.noumea.mairie.dpmmc.viewModel;

import org.zkoss.bind.annotation.NotifyChange;

public class TabModel {

    private String id;
    private String label;
    private boolean closable;
    private String sclass;
    private String zulTemplate;
    private String param;

    public TabModel(String id, String label, String zulTemplate, boolean closable, String sclass, String param) {
        this.setId(id);
        this.setLabel(label);
        this.setClosable(closable);
        this.setSclass(sclass);
        this.setZulTemplate(zulTemplate);
        this.setParam(param);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getZulTemplate() {
        return zulTemplate;
    }

    public void setZulTemplate(String zulTemplate) {
        this.zulTemplate = zulTemplate;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
