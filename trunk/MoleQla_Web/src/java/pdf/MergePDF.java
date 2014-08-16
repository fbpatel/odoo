package pdf;

/**
 *
 * @author Rafa
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilidades.OS;

public class MergePDF {

    /*public static void main(String[] args) {
     try {
     List<InputStream> pdfs = new ArrayList<InputStream>();
     pdfs.add(new FileInputStream("/home/rafa/MoleQla_Web/build/web/WEB-INF/numeros/3_0.pdf"));
     pdfs.add(new FileInputStream("/home/rafa/MoleQla_Web/build/web/WEB-INF/numeros/3_1.pdf"));
     OutputStream output = new FileOutputStream("/home/rafa/MoleQla_Web/build/web/WEB-INF/numeros/3.pdf");
     MergePDF.concatPDFs(pdfs, output, true);
     } catch (Exception e) {
     e.printStackTrace();
     }
     }*/
    public static String crearNumeroRevista(String rutaNumeros, String numero) {

        File f = new File(rutaNumeros);
        List<InputStream> pdfs = new ArrayList<InputStream>();
        String separator = OS.getDirectorySeparator();
        String numeroPDF = "";
        if (f.exists()) { // Directorio existe 

            File[] ficheros = f.listFiles();
            try {
                for (File fichero : ficheros) {
                    if (fichero.getName().equals("pdf.py")==false) {
                        pdfs.add(new FileInputStream(fichero.getPath()));
                    }
                }

                numeroPDF = rutaNumeros + separator + numero + ".pdf";
                OutputStream output = new FileOutputStream(numeroPDF);
                MergePDF.concatPDFs(pdfs, output, true);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MergePDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //Directorio no existe
        }
        return numeroPDF;
    }

    public static void concatPDFs(List<InputStream> streamOfPDFFiles,
            OutputStream outputStream, boolean paginate) {

        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + currentPageNumber + " de " + totalPages, 520,
                                5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
