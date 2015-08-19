package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.interfaces.IAgentService;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheService;
import nc.noumea.mairie.dpmmc.services.interfaces.IReferenceService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.*;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EquipageViewModel {

    @WireVariable
    private IReferenceService referenceService;

    @WireVariable
    private IAgentService agentService;

    @WireVariable
    private IFicheService ficheService;

    private Long ficheId;

    private List<Brigade> brigades;
    private Brigade selectedBrigade;

    private List<IndicatifRadio> indicatifRadios;
    private IndicatifRadio selectedIndicatifRadio;

    private List<Vehicule> vehicules;
    private Vehicule selectedVehicule;

    private List<Agent> agents = new ArrayList<>();
    private List<Agent> selectedAgents;

    private List<Patrouille> patrouilles;

    private List<Long> listIdAgents = new ArrayList<>();

    public Brigade getSelectedBrigade() {
        return selectedBrigade;
    }

    public void setSelectedBrigade(Brigade selectedBrigade) {
        this.selectedBrigade = selectedBrigade;
    }

    public IndicatifRadio getSelectedIndicatifRadio() {
        return selectedIndicatifRadio;
    }

    public void setSelectedIndicatifRadio(IndicatifRadio selectedIndicatifRadio) {
        this.selectedIndicatifRadio = selectedIndicatifRadio;
    }

    public Vehicule getSelectedVehicule() {
        return selectedVehicule;
    }

    public void setSelectedVehicule(Vehicule selectedVehicule) {
        this.selectedVehicule = selectedVehicule;
    }

    public List<Agent> getSelectedAgents() {
        return selectedAgents;
    }

    public void setSelectedAgents(List<Agent> selectedAgents) {
        this.selectedAgents = selectedAgents;
    }

    public List<Brigade> getBrigades() {
        return brigades;
    }

    public List<IndicatifRadio> getIndicatifRadios() {
        return indicatifRadios;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Patrouille> getPatrouilles() {
        return patrouilles;
    }

    public void setPatrouilles(List<Patrouille> patrouilles) {
        this.patrouilles = patrouilles;
    }

    @Init
    public void init(@BindingParam("equipages") Set<Patrouille> patrouillesArg, @BindingParam("ficheId") Long ficheId) {
        this.ficheId = ficheId;
        brigades = referenceService.getAllActive(Brigade.class, "libelleLong");
        indicatifRadios = referenceService.getAllActive(IndicatifRadio.class, "libelleLong");
        vehicules = referenceService.getAllActive(Vehicule.class, "ordre");

        if (patrouillesArg != null)
            patrouilles = new ArrayList<>(patrouillesArg);
        else
            patrouilles = new ArrayList<>();

        for (Patrouille p : patrouilles) {
            for (Agent a : p.getAgents()) {
                listIdAgents.add(a.getId());
            }
        }
    }

    @Command
    @NotifyChange({"indicatifRadios", "selectedIndicatifRadio", "selectedBrigade", "agents"})
    public void refreshLists(@BindingParam("whoChanged") String whoChanged) {

        if (whoChanged.equals("brigades")) {
            indicatifRadios = referenceService.getIndicatifRadiosFromBrigade(selectedBrigade);
            selectedIndicatifRadio = null;
        }

        if (whoChanged.equals("indicatifRadios")) {
            selectedBrigade = selectedIndicatifRadio.getBrigade();
        }

        List<Agent> agentsTmp = agentService.getAgentsFromBrigade(selectedBrigade);
        agents.clear();
        for (Agent a : agentsTmp) {
            if (!listIdAgents.contains(a.getId()))
                agents.add(a);
        }
    }

    @Command
    @NotifyChange({"patrouilles", "agents"})
    public void addPatrouilleCommand() {

        if (selectedIndicatifRadio == null) {
            throw new RuntimeException("Un code indicatif patrouille doit être sélectionné");
        }

        if (selectedVehicule == null) {
            throw new RuntimeException("Un véhicule doit être sélectionné");
        }

        if (selectedAgents == null || selectedAgents.isEmpty()) {
            throw new RuntimeException("Au moins un agent doit être sélectionné pour cette patrouille");
        }

        for (Patrouille p : patrouilles) {
            if (p.getIndicatif().getId().equals(selectedIndicatifRadio.getId()))
                throw new RuntimeException("Cet indicatif est déjà utilisé pour un équipage, veuillez supprimer l'équipage pour le réutilser");

            if (p.getVehicule().getId().equals(selectedVehicule.getId()))
                throw new RuntimeException("Ce véhicule est déjà utilisé pour un équipage, veuillez supprimer l'équipage pour le réutilser");
        }

        Patrouille p = new Patrouille();
        p.setIndicatif(selectedIndicatifRadio);
        p.setVehicule(selectedVehicule);
        p.setAgents(new HashSet<Agent>(selectedAgents));

        for (Agent a : p.getAgents()) {
            listIdAgents.add(a.getId());
            agents.remove(a);
        }

        patrouilles.add(p);
    }

    @Command
    @NotifyChange({"patrouilles", "agents"})
    public void removePatrouilleCommand(@BindingParam("item") Patrouille element) {
        if (element == null)
            return;

        Patrouille toRemove = null;

        for (Patrouille p : patrouilles) {

            if ((p.getId() == null || (p.getId().equals(element.getId()) && element.getId() != 0l))
                    || (p.getIndicatif().getId().equals(element.getIndicatif().getId())
                    && p.getVehicule().getId().equals(element.getVehicule().getId()))) {
                toRemove = p;
            }
        }

        if (toRemove != null) {
            patrouilles.remove(toRemove);
            for (Agent a : toRemove.getAgents()) {
                listIdAgents.remove(a.getId());
                agents.add(a);
            }
        }
    }

    @Command
    public void save(@BindingParam("win") Window x) {
        x.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("equipages", patrouilles);
        args.put("ficheId", ficheId);
        BindUtils.postGlobalCommand(null, null, "updateEquipagesCommand", args);
    }
}
