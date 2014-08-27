package pdf;

/**
 *
 * @author Rafa
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
    public static String crearNumeroRevista(String rutaRaiz, String rutaNumeros, String numero) {
        String separator = OS.getDirectorySeparator();

        //Portada
        String ruta_portada = rutaRaiz + "WEB-INF" + separator + "numeros" + separator + "portada.pdf";
        File portada = new File(ruta_portada);

        //Participantes
        String ruta_participantes = rutaRaiz + "WEB-INF" + separator + "numeros" + separator + "participantes.pdf";
        File participantes = new File(ruta_participantes);

        //Indice
        String ruta_indice = rutaRaiz + "WEB-INF" + separator + "numeros" + separator + "indice.pdf";
        File indice = new File(ruta_indice);

        //Entrevista
        String ruta_entrevista = rutaRaiz + "WEB-INF" + separator + "numeros" + separator + "entrevista.pdf";
        File entrevista = new File(ruta_entrevista);

        //Contraportada
        String ruta_contraportada = rutaRaiz + "WEB-INF" + separator + "numeros" + separator + "contraportada.pdf";
        File contraportada = new File(ruta_contraportada);

        File f = new File(rutaNumeros);
        List<InputStream> pdfs = new ArrayList<InputStream>();

        try {
            //Se añade la portada
            pdfs.add(new FileInputStream(portada.getPath()));

            //Se añade el fichero de las personas participantes
            pdfs.add(new FileInputStream(participantes.getPath()));

            //Se añade el indice
            pdfs.add(new FileInputStream(indice.getPath()));

            if (entrevista.exists()) {
                //Se añade la entrevista si existe
                pdfs.add(new FileInputStream(entrevista.getPath()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MergePDF.class.getName()).log(Level.SEVERE, null, ex);
        }

        String numeroPDF = "";
        if (f.exists()) { // Directorio existe 

            //Se añaden los ficheros a una lista para poder ordenadrla
            File[] ficheros = f.listFiles();
            List<File> listaAllArticulos = Arrays.asList(ficheros);

            // A continuación, se va a ordenador la lista por orden
            Collections.sort(listaAllArticulos, new Comparator<File>() {
                @Override
                public int compare(File u1, File u2) {
                    //cadena.substring(0, cadena.length()-1); 
                    String uu1 = u1.getName().substring(0, u1.getName().length() - 4);
                    String uu2 = u2.getName().substring(0, u2.getName().length() - 4);

                    return uu1.compareTo(uu2);//de menor a mayor
                    //return uu2.compareTo(uu1);//de mayor a menor
                }
            });

            try {
                //Se añaden todos los articulos a la lista pdfs
                for (File fichero : listaAllArticulos) {
                    pdfs.add(new FileInputStream(fichero.getPath()));
                }

                //Se añade la contraportada
                pdfs.add(new FileInputStream(contraportada.getPath()));

                //Se monta el numero
                numeroPDF = rutaRaiz + "revista" + separator + "work" + separator + numero + ".pdf";
                OutputStream output = new FileOutputStream(numeroPDF);
                MergePDF.concatPDFs(pdfs, output, true);
                borrarFicherosDinamicos(rutaRaiz + "WEB-INF" + separator + "numeros" + separator);
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

            }
            //Se calculan el numero total de articulos
            totalPages = pdfs.size() - 4;

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
            int numPage = 0;
            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                if (currentPageNumber != 0 && currentPageNumber != 1 && currentPageNumber != 2
                        && numPage != totalPages) {
                    paginate = true;
                    numPage++;
                } else {
                    paginate = false;
                }

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
                                + numPage + " de " + totalPages, 520,
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

    private static void borrarFicherosDinamicos(String rutaNumeros) {
        //Participantes
        String ruta_participantes = rutaNumeros + "participantes.pdf";
        File participantes = new File(ruta_participantes);
        if (participantes.exists()) {
            participantes.delete();
        }

        //Indice
        String ruta_indice = rutaNumeros + "indice.pdf";
        File indice = new File(ruta_indice);
        if (indice.exists()) {
            indice.delete();
        }

        //Entrevista
        String ruta_entrevista = rutaNumeros + "entrevista.pdf";
        File entrevista = new File(ruta_entrevista);
        if (entrevista.exists()) {
            entrevista.delete();
        }
    }
}
