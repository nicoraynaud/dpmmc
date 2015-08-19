package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Profil;
import nc.noumea.mairie.dpmmc.services.interfaces.IProfilService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import java.util.HashMap;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProfilViewModel {

    @WireVariable
    private IProfilService profilService;

    private ListModel<Profil> profils;

    public ListModel<Profil> getProfils() {
        return profils;
    }

    @Init
    @NotifyChange({ "profils" })
    public void initLists() {
        profils = new ListModelList<Profil>(profilService.getAllProfils());
    }

    @Command
    public void itemChanged(@BindingParam("item") Profil profil) {
        Profil p = profilService.save(profil);
        profil.setId(p.getId());
        Clients.showNotification("Le profil a été mis à jour avec succès.", "info", null, "bottom_right", 1500);
    }

    @Command
    public void createProfil() {
        Profil p = new Profil();
        p.setNom("Nouveau profil #" + (profils.getSize() +1 ));
        ((ListModelList) profils).add(0, p);
    }
}
