package nc.noumea.mairie.dpmmc.viewModel;

import nc.noumea.mairie.dpmmc.domain.Agent;
import nc.noumea.mairie.dpmmc.domain.ErreurGeolocalisationVP;
import nc.noumea.mairie.dpmmc.domain.GeolocalisationVP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeolocFicheModel {

    private Date gdhAppel;
    private String vp;
    private String agents;
    private String adresse;
    private Long idGeolocalisationVP;
    private List<String> erreurs;

    public Date getGdhAppel() {
        return gdhAppel;
    }

    public String getVp() {
        return vp;
    }

    public String getAgents() {
        return agents;
    }

    public String getAdresse() {
        return adresse;
    }

    public Long getIdGeolocalisationVP() {
        return idGeolocalisationVP;
    }

    public List<String> getErreurs() {
        return erreurs;
    }

    public GeolocFicheModel(GeolocalisationVP gvp) {
        gdhAppel = gvp.getDateHeure();
        vp = gvp.getIndicatifRadio().getLibelleLong();
        adresse = String.format("%s %s\n\r%s", gvp.getAdresse().getNumeroVoie(),
                gvp.getAdresse().getNomVoie().getLibelle(),
                gvp.getAdresse().getQuartier().getLibelle());
        idGeolocalisationVP = gvp.getId();
        agents = "";
        for (Agent a : gvp.getAgents()) {
            agents = String.format("%s %s %s\n\r", agents, a.getNom(), a.getPrenom());
        }

        erreurs = new ArrayList<String>();
        for (ErreurGeolocalisationVP e : gvp.getErreursGeocalisationVP()) {
            erreurs.add(e.getLibelle());
        }
    }

}
