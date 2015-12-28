package nc.noumea.mairie.dpmmc.services.interfaces;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.domain.Fiche;
import nc.noumea.mairie.dpmmc.viewModel.FicheModel;
import nc.noumea.mairie.dpmmc.viewModel.FicheSearchResultModel;
import org.springframework.security.access.annotation.Secured;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public interface IFicheService {

    List<FicheSearchResultModel> getLastFichesCloturees();

    List<FicheSearchResultModel> getLastActivitiesWithAlerte();

    @Secured({"ROLE_RECH_FICHE"})
    List<FicheSearchResultModel> search(String numeroFiche, Long agentId, Long brigadeId, Long natureId, Long categorieId, Long secteurId, Long quartierId, Long lieuPredefiniId, Long categoriePersonneId, String nomPersonne, Date dateSaisieStart, Date dateSaisieEnd);

    @Secured({"ROLE_RECH_FICHE"})
    byte[] exportFicheEventReport(String numeroFiche, Long agentId, Long brigadeId, Long natureId, Long categorieId, Long secteurId, Long quartierId, Long lieuPredefiniId, Long categoriePersonneId, String nomPersonne, Date dateSaisieStart, Date dateSaisieEnd);

    void saveFiche(Fiche fiche);

    FicheModel getFiche(Long idFiche);

    @Secured({"ROLE_CREATION_FICHE"})
    Long createFicheFromBlank();

    @Secured({"ROLE_CREATION_FICHE"})
    Long createFicheFromGeolocalisationVP(Long idGelocalisationVP);

    @Secured({"ROLE_CLOTURE_FICHE"})
    void clotureFiche(Long idFiche);

    @Secured({"ROLE_DECLOTURE_FICHE"})
    void declotureFiche(Long idFiche);

    @Secured({"ROLE_SUPPR_FICHE"})
    void supprimeFiche(Long idFiche, String motif);

    byte[] getFicheReport(Long idFiche) throws DocumentException;
}
