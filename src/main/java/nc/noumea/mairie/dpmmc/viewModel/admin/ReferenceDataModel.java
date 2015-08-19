package nc.noumea.mairie.dpmmc.viewModel.admin;

public class ReferenceDataModel {

    private String name;
    private Class clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ReferenceDataModel(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public ReferenceDataModel() {

    }

    @Override
    public String toString() {
        return name;
    }
}
