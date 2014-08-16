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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            String rutaWEBINF = formBean.getRutaNumeros();

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String nombreNum = dateFormat.format(date);

            if (consultaListaNumeros(rutaWEBINF, nombreNum) == false) {
                formBean.setErrorMsg(Constantes.getERROR_CREAR_NUMERO());
                return mapping.findForward(FAILURE);
            }
            formBean.setMsg(Constantes.getCREACION_NUMERO_OK());
            return mapping.findForward(SUCCESS);
        }
    }

    private boolean consultaListaNumeros(String rutaWEBINF, String nombreNum) throws SQLException {
        String separator = OS.getDirectorySeparator();
        List<InputStream> listaNumerosArt = null;
        List<String> listaNombresArt = new ArrayList();
        boolean res = true;

        String fichero = rutaWEBINF + separator + "numeros" + separator + "pdf.py";
        String rutaDestino = rutaWEBINF + separator + "numeros";
        String[] cmd = new String[2];
        cmd[0] = fichero;
        cmd[1] = rutaDestino;

        Process f;
        try {
            f = Runtime.getRuntime().exec(cmd);
            try {
                f.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(CrearRevistaAction.class.getName()).log(Level.SEVERE, null, ex);
            }

            // retrieve output from python script
            BufferedReader bfr = new BufferedReader(new InputStreamReader(f.getInputStream()));
            String numero = "";
            while ((numero = bfr.readLine()) != null) {
                System.out.println(numero);
            }
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
