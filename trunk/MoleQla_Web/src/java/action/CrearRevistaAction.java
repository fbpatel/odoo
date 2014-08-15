/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.CrearRevistaActionForm;
import actionForm.RegistroActionForm;
import connection.ConnectionPSQL;
import email.Mail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pdf.MergePDF;
import utilidades.Constantes;
import utilidades.GenerarCadenaAlfanumerica;
import utilidades.OS;

/**
 *
 * @author Rafa
 */
public class CrearRevistaAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private final static String CANCEL = "cancel";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward(CANCEL);
        } else {
            CrearRevistaActionForm formBean = (CrearRevistaActionForm) form;
            String rutaNumeros = formBean.getRutaNumeros();

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String nombreNum = dateFormat.format(date);

            if (consultaListaNumeros(rutaNumeros, nombreNum) == false) {
                formBean.setErrorMsg(Constantes.getERROR_CREAR_NUMERO());
                return mapping.findForward(FAILURE);
            }
            formBean.setMsg(Constantes.getCREACION_NUMERO_OK());
            return mapping.findForward(SUCCESS);
        }
    }

    private boolean consultaListaNumeros(String rutaDestino, String nombreNum) throws SQLException {
        List<InputStream> listaNumerosArt = null;
        List<String> listaNombresArt = new ArrayList();
        boolean res = true;

        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT archivo, numero_id FROM articulo "
                    + "WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = '" + Constantes.getESTADO_NUMEROS_PUBLICAR() + "'");

            listaNumerosArt = new ArrayList();
            byte[] imgBytes = null;
            int numero_id = 0, i = 0;
            String separator = OS.getDirectorySeparator();
            String nombreArt = "";
            if (rs != null) {
                while (rs.next()) {
                    imgBytes = rs.getBytes(1);
                    numero_id = rs.getInt(2);
                    nombreArt = String.valueOf(numero_id) + "_" + String.valueOf(i);

                    FileOutputStream os = new FileOutputStream(rutaDestino + separator + nombreArt + ".pdf");
                    os.write(imgBytes);
                    os.flush();
                    os.close();

                    // Guardamos la ruta de los articulos para luego poder borrarlos
                    listaNombresArt.add(rutaDestino + separator + nombreArt + ".pdf");

                    //Añadimos los pdf a la lista
                    FileInputStream pdf = new FileInputStream(rutaDestino + separator + nombreArt + ".pdf");
                    listaNumerosArt.add(pdf);
                    pdf.close();

                    i++;
                }
                rs.close();
            }
            connection.close();

            //Vamos a crear el numero final
            try {
                String rutaResultado = rutaDestino + separator + nombreNum + ".pdf";
                OutputStream output = new FileOutputStream(rutaResultado);
                MergePDF.concatPDFs(listaNumerosArt, output, true);

                res = comprobacion_eliminacion(rutaResultado, listaNombresArt);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("\n" + e.getMessage()
                    + "\n-----------------\n"
                    + Constantes.getERROR_CREAR_NUMERO()
                    + "\n-----------------\n");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrearRevistaAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearRevistaAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    private boolean comprobacion_eliminacion(String rutaDestino, List<String> listaNombresArt) {
        File resNum = new File(rutaDestino);
        boolean borrado = true;
        if (resNum.exists()) {

            int i = 0;
            while (i < listaNombresArt.size() && borrado == true) {
                File pdf = new File(listaNombresArt.get(i));
                borrado = pdf.delete();
            }
        }

        return borrado;
    }
}
