package nc.noumea.mairie.dpmmc.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "VM_GEOLOC")
@NamedQuery(name = "getDonneesBrutesSinceDate", query = "from DonneeBrutGeoloc where action = 'N' and dateHeure > :date order by dateHeure desc")
public class DonneeBrutGeoloc {

    @Id
    @Column(name = "date_time")
    private Date dateHeure;

    @Column(name = "action")
    private char action;

    @Column(name = "id_patrouille")
    private String libelleIndicatifRadio;

    @Column(name = "vp")
    private String immatriculationVehicule;

    @Column(name = "equipage")
    private String listeMatriculesAgents;

    @Column(name = "idadrs")
    private String idAdresse;

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    public char getAction() {
        return action;
    }

    public void setAction(char action) {
        this.action = action;
    }

    public String getLibelleIndicatifRadio() {
        return libelleIndicatifRadio;
    }

    public void setLibelleIndicatifRadio(String libelleIndicatifRadio) {
        this.libelleIndicatifRadio = libelleIndicatifRadio;
    }

    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    public String getListeMatriculesAgents() {
        return listeMatriculesAgents;
    }

    public void setListeMatriculesAgents(String listeMatriculesAgents) {
        this.listeMatriculesAgents = listeMatriculesAgents;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }

    @Override
    public String toString() {
        String donneeBrut = "";
        donneeBrut += "DATE_TIME: " + dateHeure.toString() + " | ";
        donneeBrut += "ACTION: " + action + " | ";
        donneeBrut += "ID_PATROUILLE: " + libelleIndicatifRadio + " | ";
        donneeBrut += "VP: " + immatriculationVehicule + " | ";
        donneeBrut += "EQUIPAGE: " + listeMatriculesAgents + " | ";
        donneeBrut += "IDADRS: " + idAdresse;
        return donneeBrut;
    }
}
