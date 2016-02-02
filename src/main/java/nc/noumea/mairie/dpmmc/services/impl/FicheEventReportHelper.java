package nc.noumea.mairie.dpmmc.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.BusinessException;
import nc.noumea.mairie.dpmmc.services.interfaces.IAuthHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheEventReportHelper;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class FicheEventReportHelper implements IFicheEventReportHelper {

    private Logger logger = LoggerFactory.getLogger(FicheEventReportHelper.class);

    private Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private Font FONT_TITLE_LIGHT = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
    private Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    private Font FONT_PARAM_BOLD = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private Font FONT_PARAM = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

    @Autowired
    private IAuthHelper authHelper;

    @Autowired
    private ApplicationContext appContex;

    @Override
    public byte[] buildReportFromFiches(List<Fiche> fiches, Map<String, String> searchParams) {

        logger.debug("Début exportation rapport Fiche Evenementielle");

        if (fiches.size() == 0) {
            throw new BusinessException("Aucune fiche à imprimer.");
        }

        ByteArrayOutputStream bstream = new ByteArrayOutputStream();

        logger.debug("Génération du PDF");

        try {
            String date = new DateTime().toString("dd/MM/YYYY HH:mm");
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, bstream);
            FicheEventReportPageTemplate event = new FicheEventReportPageTemplate();
            event.setFooter(date);
            writer.setPageEvent(event);
            document = buildMetadata(document, date);

            document.open();
            document.add(buildHeader(date));
            document.add(buildSearchParams(searchParams));
            document.add(buildTable(fiches));

            document.close();

        } catch(DocumentException | IOException ex) {
            logger.error("Une erreur est survenue lors de l'impression du rapport Fiche Evenementielle", ex);
            throw new BusinessException("Une erreur est survenue lors de l'impression du rapport. Veuillez consulter votre administrateur.", ex);
        }

        logger.debug("Fin de génération");

        return bstream.toByteArray();
    }

    private Document buildMetadata(Document document, String date) {
        String author = authHelper.getCurrentUser().getAgent().getNom() + " " + authHelper.getCurrentUser().getAgent().getPrenom();
        document.addAuthor(author);
        document.addCreationDate();
        document.addCreator(author);
        document.addTitle("FICHE EVENEMENTIELLE " + date);
        document.addSubject("FICHE EVENEMENTIELLE " + date);

        return document;
    }

    private PdfPTable buildHeader(String date) throws BadElementException, IOException {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);

        Resource r = appContex.getResource("/img/logo_mairie.png");
        Image img = Image.getInstance(r.getFile().getAbsolutePath());
        cell = new PdfPCell();
        cell.addElement(new Chunk(img, 60, 0));
        cell.setFixedHeight(70);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(23);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setRowspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FICHE EVENEMENTIELLE", FONT_TITLE));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        Resource r2 = appContex.getResource("/img/logo_police_nc.jpg");
        Image img2 = Image.getInstance(r2.getFile().getAbsolutePath());
        cell = new PdfPCell();
        cell.addElement(new Chunk(img2, 130, 0));
        cell.setFixedHeight(70);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(23);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setRowspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fiche éditée le " + date + "\npar " +
                authHelper.getCurrentUser().getAgent().getNom() + " " +
                authHelper.getCurrentUser().getAgent().getPrenom(), FONT_TITLE_LIGHT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildSearchParams(Map<String, String> searchParams) throws DocumentException {

        PdfPTable table = new PdfPTable(2);
        int[] widths = {18, 82};
        table.setWidths(widths);
        table.setWidthPercentage(100);

        if (searchParams.keySet().size() == 0)
            return table;

        PdfPCell cell = new PdfPCell(new Phrase("Filtres", FONT_PARAM_BOLD));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        for (String key : searchParams.keySet()) {
            cell = new PdfPCell(new Phrase(key, FONT_PARAM));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(searchParams.get(key), FONT_PARAM));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }


        table.setSpacingAfter(10);

        return table;
    }

    private PdfPTable buildTable(List<Fiche> fiches) throws DocumentException {

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        int[] widths = {14, 19, 17, 15, 20, 30, 30, 13, 33};
        table.setWidths(widths);
        table.setHeaderRows(1);

        PdfPCell cell = new PdfPCell(new Phrase("N° Fiche", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date-Heure", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nature des faits et nombre", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Equipages", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quartier", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Lieu", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Personnes concernées", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Procédures", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Situation initiale", FONT_NORMAL));
        cell.setFixedHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm");

        for (Fiche f : fiches) {
            Historique creation = f.getHistoriques().iterator().next();

            // N° Fiche
            cell = new PdfPCell(new Phrase(f.getReference(), FONT_NORMAL));
            table.addCell(cell);

            // Date/Heure
            cell = new PdfPCell(new Phrase(df.format(creation.getDateAction()), FONT_NORMAL));
            table.addCell(cell);

            // Faits
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Fait fa : f.getFaits()) {
                if (first) {
                    first = false;
                } else {
                    sb.append("\n");
                }
                sb.append(fa.getNombre() + " " + fa.getNature().getLibelleCourt());
            }
            cell = new PdfPCell(new Phrase(sb.toString(), FONT_NORMAL));
            table.addCell(cell);

            // Equipages
            sb = new StringBuilder();
            first = true;
            for (Patrouille pa : f.getPatrouilles()) {
                if (first) {
                    first = false;
                } else {
                    sb.append("\n");
                }
                sb.append(pa.getIndicatif().getLibelleLong());
            }
            cell = new PdfPCell(new Phrase(sb.toString(), FONT_NORMAL));
            table.addCell(cell);

            // Quartier
            cell = new PdfPCell(new Phrase(f.getAdresse().getQuartier().getLibelle(), FONT_NORMAL));
            table.addCell(cell);

            // Lieu
            cell = new PdfPCell(new Phrase(f.getAdresse().getNumeroVoie() + " " + f.getAdresse().getNomVoie().getLibelle(), FONT_NORMAL));
            table.addCell(cell);

            // Personnes
            sb = new StringBuilder();
            first = true;
            for (Personne pe : f.getPersonnes()) {
                if (first) {
                    first = false;
                } else {
                    sb.append("\n");
                }
                sb.append(pe.getCategorie().getLibelleLong() + " : " + (StringUtils.isEmpty(pe.getNom()) ? "-" : pe.getNom()));
            }
            cell = new PdfPCell(new Phrase(sb.toString(), FONT_NORMAL));
            table.addCell(cell);

            // Procedures
            sb = new StringBuilder();
            first = true;
            for (Procedure pr : f.getProcedures()) {
                if (first) {
                    first = false;
                } else {
                    sb.append("\n");
                }
                sb.append(pr.getCategorie().getLibelleCourt() + " - " + pr.getNumero());
            }
            cell = new PdfPCell(new Phrase(sb.toString(), FONT_NORMAL));
            table.addCell(cell);

            // Situation initiale + détail intervention
            String situInitiale;
            if (StringUtils.isEmpty(f.getSituationInitialeTitre())){
                situInitiale = "pas de détails";
            } else {
                situInitiale = f.getSituationInitialeTitre();
            }

            cell = new PdfPCell(new Phrase(situInitiale, FONT_NORMAL));
            table.addCell(cell);

        }

        return table;
    }


}
