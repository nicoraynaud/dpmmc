package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.lang.reflect.Field;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ReferenceViewModel<T> {

    @WireVariable
    protected IReferenceService referenceService;

    protected Class<T> clazz;

    protected String defaultField;
    protected boolean isFieldInParent;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    @Init
    public void init() {
        data = referenceService.getAll(clazz, "id");
    }

    @Command
    public void itemChanged(@BindingParam("item") T item) {
        T i = referenceService.save(clazz, item);

        try {
            Field field;
            if (isFieldInParent)
                field = clazz.getSuperclass().getDeclaredField("id");
            else
                field = clazz.getDeclaredField("id");

            field.setAccessible(true);
            field.set(item, field.get(i));
            Clients.showNotification("L'élément a été mis à jour avec succès.", "info", null, "bottom_right", 1500);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Clients.showNotification("La mise à jour a échoué.", "error", null, "bottom_right", 0, true);
        }
    }

    @Command
    @NotifyChange("data")
    public void addItem() {

        try {
            T u = clazz.newInstance();

            if (StringUtils.isNotBlank(defaultField)) {
                Field field;

                if (isFieldInParent)
                    field = clazz.getSuperclass().getDeclaredField(defaultField);
                else
                    field = clazz.getDeclaredField(defaultField);

                field.setAccessible(true);
                field.set(u, String.format("[Nouveau] %s #%d", clazz.getSimpleName(), data.size() + 1));
            }
            data.add(0, u);
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            Clients.showNotification("La création de l'élément a échoué.", "error", null, "bottom_right", 0, true);
        }
    }
}
