package nc.noumea.mairie.dpmmc.services.interfaces;

import com.itextpdf.text.DocumentException;
import nc.noumea.mairie.dpmmc.domain.Fiche;

import java.io.ByteArrayOutputStream;

public interface IFicheReportHelper {

    byte[] buildReportFromFiche(Fiche f) throws DocumentException;
}
