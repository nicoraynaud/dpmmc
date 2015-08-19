package nc.noumea.mairie.dpmmc.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import nc.noumea.mairie.dpmmc.domain.*;
import nc.noumea.mairie.dpmmc.services.BusinessException;
import nc.noumea.mairie.dpmmc.services.interfaces.IAuthHelper;
import nc.noumea.mairie.dpmmc.services.interfaces.IFicheReportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FicheReportHelper implements IFicheReportHelper {

    private Logger logger = LoggerFactory.getLogger(GeolocService.class);

    private Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private Font FONT_NORMAL_WHITE = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.WHITE);
    private Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private Font FONT_BOLD_WHITE = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private Font FONT_SPACER = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.WHITE);

    @Autowired
    private IAuthHelper authHelper;

    @Override
    public byte[] buildReportFromFiche(Fiche fiche) throws DocumentException {

        logger.debug("Début méthode exportation PDF");

        if (fiche.getAdresse() == null) {
            throw new BusinessException("Impossible d'imprimer cette fiche : L'adresse n'est pas remplie.");
        }

        ByteArrayOutputStream bstream = new ByteArrayOutputStream();

        logger.debug("Génération du PDF");

        Document document = new Document();
        PdfWriter.getInstance(document, bstream);
        document = buildMetadata(document, fiche);

        document.open();
        document.add(buildHeader(fiche));
        document.add(buildInfos(fiche));
        document.add(buildSaisine(fiche));
        document.add(buildExpose(fiche));
        document.add(buildPersonnes(fiche));
        document.add(buildProcedures(fiche));

        document.close();

        logger.debug("Fin de génération");

        return bstream.toByteArray();

    }

    private Document buildMetadata(Document document, Fiche fiche) {
        Agent a = fiche.getHistoriques().iterator().next().getUtilisateur().getAgent();
        document.addAuthor(a.getNom() + " " + a.getPrenom());
        document.addCreationDate();
        document.addCreator(authHelper.getCurrentUser().getAgent().getNom() + " " + authHelper.getCurrentUser().getAgent().getPrenom());
        document.addTitle("MAIN COURANTE " + fiche.getReference());
        document.addSubject("MAIN COURANTE " + fiche.getReference());

        StringBuilder keywords = new StringBuilder();
        keywords.append(fiche.getReference());
        keywords.append(",");
        keywords.append(fiche.getAdresse().getNomVoie().getLibelle());
        keywords.append(",");
        for(Fait f : fiche.getFaits()) {
            keywords.append(f.getNature().getLibelleCourt());
            keywords.append(",");
        }
        document.addKeywords(keywords.toString());

        return document;
    }

    private PdfPTable buildHeader(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("POLICE MUNICIPALE", FONT_BOLD_WHITE));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.BLACK);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("MAIN COURANTE", FONT_TITLE));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRowspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("N° fiche: " + fiche.getReference(), FONT_NORMAL_WHITE));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.BLACK);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NOUVELLE-CALÉDONIE", FONT_BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Ville de Nouméa", FONT_BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildInfos(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(new float[]{1, 2});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("NATURE DES FAITS: ", FONT_BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);


        if (fiche.getFaits() != null && fiche.getFaits().size() != 0) {

            String libelleFaits = "";

            for (Fait fait : fiche.getFaits()) {
                libelleFaits += ((libelleFaits == "") ? "" : ", ") + fait.getNombre() + " " + fait.getNature().getLibelleCourt();
            }

            cell = new PdfPCell(new Phrase(libelleFaits, FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

        } else {
            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        }


        cell = new PdfPCell(new Phrase("Quartier: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColorBottom(BaseColor.WHITE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(fiche.getAdresse().getQuartier().getLibelle(), FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Secteur: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColorTop(BaseColor.WHITE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(fiche.getAdresse().getQuartier().getSecteur().getLibelle(), FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildSaisine(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(new float[]{1, 2.6f, 1.2f, 1.2f});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("1_ Saisine ", FONT_NORMAL));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Agent Créateur: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        Agent a = fiche.getHistoriques().iterator().next().getUtilisateur().getAgent();
        cell = new PdfPCell(new Phrase(a.getNom() + " " + a.getPrenom(), FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cat. saisine: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        String catSaisine = fiche.getCategorieSaisine().getLibelleLong();

        cell = new PdfPCell(new Phrase(catSaisine, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Contact interpolice: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        String contactInterpolice = fiche.getContactInterpolice().getLibelleLong();

        cell = new PdfPCell(new Phrase(contactInterpolice, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Equipages: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        boolean premierePatrouille = true;

        if (fiche.getPatrouilles() == null || fiche.getPatrouilles().size() == 0) {
            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
        }

        for (Patrouille patrouille : fiche.getPatrouilles()) {

            if (!premierePatrouille) {

                cell = new PdfPCell(new Phrase("", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            } else {
                premierePatrouille = false;
            }

            String libelleEquipage = patrouille.getIndicatif().getLibelleLong();
            libelleEquipage = libelleEquipage.concat(" : ");
            if (patrouille.getAgents() != null && patrouille.getAgents().size() > 0) {
                int cpt = 1;
                for (Agent agentPatrouille : patrouille.getAgents()) {
                    libelleEquipage = libelleEquipage.concat(agentPatrouille.getNom() + " " + agentPatrouille.getPrenom());
                    if (cpt != patrouille.getAgents().size()) {
                        libelleEquipage = libelleEquipage.concat(", ");
                        cpt++;
                    }
                }
            }

            cell = new PdfPCell(new Phrase(libelleEquipage, FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

            String vehicule = "";
            if (patrouille.getVehicule() != null) {
                vehicule = patrouille.getVehicule().getImmatriculation();
            }
            cell = new PdfPCell(new Phrase(vehicule, FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

        }


        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildExpose(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(new float[]{1, 0.5f, 4.5f});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("2_ Exposé des faits ", FONT_NORMAL));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("GDH Appel: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        Date gdhAppelDate = fiche.getGdhAppel();

        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String gdhAppel = dateFormat.format(gdhAppelDate);

        cell = new PdfPCell(new Phrase(gdhAppel, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("GDH Arrivée: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        Date gdhArriveeDate = fiche.getGdhArrivee();
        String gdhArrivee = "";
        if (gdhArriveeDate != null) gdhArrivee = dateFormat.format(gdhArriveeDate);

        cell = new PdfPCell(new Phrase(gdhArrivee, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Lieu: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        String numeroRue = fiche.getAdresse().getNumeroVoie();

        cell = new PdfPCell(new Phrase(numeroRue, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        String libelleRue = fiche.getAdresse().getNomVoie().getLibelle();

        cell = new PdfPCell(new Phrase(libelleRue, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Précisions: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        String lieuPredefini = "";
        if (fiche.getLieuPredefini() != null)
            lieuPredefini = fiche.getLieuPredefini().getLibelleLong().concat(" ");
        String adresseComplement = "";
        if (fiche.getComplementAdresse() != null)
            adresseComplement = fiche.getComplementAdresse();
        cell = new PdfPCell(new Phrase(lieuPredefini.concat(adresseComplement), FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("spacer", FONT_SPACER));
        cell.setBorder(0);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Situation initiale: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        String situationInitiale = fiche.getSituationInitiale();

        cell = new PdfPCell(new Paragraph(situationInitiale, FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("spacer", FONT_SPACER));
        cell.setBorder(0);
        cell.setColspan(3);
        table.addCell(cell);

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildPersonnes(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(new float[]{1, 1.5f, 2.5f, 1});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("3_ Personnes concernées", FONT_NORMAL));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        table.addCell(cell);

        /**
         * Appelants
         */
        cell = new PdfPCell(new Phrase("Requérants: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        boolean premierAppelant = true;

        if (fiche.getAppelants() == null || fiche.getAppelants().size() == 0) {
            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        }

        for (Appelant appelant : fiche.getAppelants()) {

            if (!premierAppelant) {

                cell = new PdfPCell(new Phrase("", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);
            } else {
                premierAppelant = false;
            }

            cell = new PdfPCell(new Phrase(appelant.getRequerant(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(appelant.getNumero(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

        }

        /**
         * Personnes
         */
        cell = new PdfPCell(new Phrase("Personnes: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        boolean premierePersonne = true;

        if (fiche.getPersonnes() == null || fiche.getPersonnes().size() == 0) {
            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);
        }

        for (Personne personne : fiche.getPersonnes()) {

            if (!premierePersonne) {

                cell = new PdfPCell(new Phrase("", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            } else {
                premierePersonne = false;
            }

            cell = new PdfPCell(new Phrase((personne.getNombre() == null ? "" : (personne.getNombre() + " - ")) + personne.getCategorie().getLibelleLong(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(personne.getNom() + " - " + personne.getDetails(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

        }

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

    private PdfPTable buildProcedures(Fiche fiche) {
        PdfPCell cell;
        PdfPTable table = new PdfPTable(new float[]{1, 3, 1, 1});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("4_ Procédures", FONT_NORMAL));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        table.addCell(cell);

        /**
         * Procedures
         */
        cell = new PdfPCell(new Phrase("Catégories: ", FONT_NORMAL));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        table.addCell(cell);

        boolean premiereProcedure = true;

        if (fiche.getProcedures() == null || fiche.getProcedures().size() == 0) {
            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Numéros: ", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        }

        for (Procedure procedure : fiche.getProcedures()) {

            if (!premiereProcedure) {

                cell = new PdfPCell(new Phrase("", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            }

            cell = new PdfPCell(new Phrase(procedure.getCategorie().getLibelleLong(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            if (!premiereProcedure) {

                cell = new PdfPCell(new Phrase("", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            } else {

                cell = new PdfPCell(new Phrase("Numéros: ", FONT_NORMAL));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                premiereProcedure = false;
            }

            cell = new PdfPCell(new Phrase(procedure.getNumero(), FONT_NORMAL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

        }

        table.setSpacingBefore(5);
        table.setSpacingAfter(5);

        return table;
    }

}
