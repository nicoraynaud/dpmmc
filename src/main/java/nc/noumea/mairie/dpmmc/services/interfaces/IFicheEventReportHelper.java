package nc.noumea.mairie.dpmmc.services.interfaces;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.domain.Fiche;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFicheEventReportHelper {

    byte[] buildReportFromFiches(List<Fiche> fiches, Map<String, String> searchParams);
}
