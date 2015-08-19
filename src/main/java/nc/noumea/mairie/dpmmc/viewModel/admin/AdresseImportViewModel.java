package nc.noumea.mairie.dpmmc.viewModel.admin;

import nc.noumea.mairie.dpmmc.services.interfaces.IAdresseImportHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AdresseImportViewModel {

    @WireVariable
    private IAdresseImportHelper adresseImportHelper;

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    @Command
    @NotifyChange({"errors"})
    public void uploadAdresseFileCommand(@BindingParam("event") UploadEvent uploadEvent) {
        List<String> messages = null;

        try {
            messages = adresseImportHelper.importAddressFromFile(uploadEvent.getMedia());
        } catch (Exception e) {
            Clients.showNotification(String.format("Une erreur s'est produite pendant l'import des adresses : \n\n%s", e.toString()), "error", null, "bottom_right", 0, true);
            return;
        }

        if (messages.isEmpty()) {
            Clients.showNotification("Import des adresses réussi.", "info", null, "bottom_right", 1500);
        } else {
            Clients.showNotification("Des erreurs se sont produires pendant l'import des adresses.", "error", null, "bottom_right", 0, true);
        }
    }

}
