package nc.noumea.mairie.dpmmc.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class FicheEventReportPageTemplate extends PdfPageEventHelper {

    private Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);

    /**
     * The footer text.
     */
    String footer;
    /**
     * The template with the total number of pages.
     */
    PdfTemplate total;

    /**
     * Allows us to change the content of the footer.
     *
     * @param footer The new footer String
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    /**
     * Adds a footer to every page
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{24, 24, 2});
            table.setTotalWidth(770);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(new Phrase(footer, FOOTER_FONT));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(new Phrase(String.format("Page %d sur ", writer.getPageNumber()), FOOTER_FONT));
            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            table.writeSelectedRows(0, -1, document.left(), document.bottom(), writer.getDirectContent());
            //document.add(table);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the total number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber() - 1), FOOTER_FONT),
                2, 5, 0);
    }
}

