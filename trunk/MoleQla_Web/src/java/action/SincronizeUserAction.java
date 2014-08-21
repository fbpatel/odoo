/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.CrearRevistaActionForm;
import actionForm.RegistroActionForm;
import actionForm.SincronizeUserActionForm;
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
import java.nio.channels.FileChannel;
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
import utilidades.BorrarDirectorio;
import utilidades.Constantes;
import utilidades.OS;

/**
 *
 * @author Rafa
 */
public class SincronizeUserAction extends org.apache.struts.action.Action {

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
            SincronizeUserActionForm formBean = (SincronizeUserActionForm) form;
            String rutaRaiz = formBean.getRutaRaiz();

            //Priemro ahcemos una copia de seguridad de las fotos
            guardaCopiaSeguridad(rutaRaiz);

            //Ahora creamos las fotos
            consultaFotos(rutaRaiz);
            
            //Copiar las fotos a /revista/about/fotos
            copiarFotos(rutaRaiz);

            formBean.setMsg(Constantes.getCREACION_NUMERO_OK());
            return mapping.findForward(SUCCESS);
        }
    }

    private void consultaFotos(String rutaRaiz) throws SQLException {
        String separator = OS.getDirectorySeparator();

        String fichero = rutaRaiz + "WEB-INF" + separator + "about" + separator + "about.py";
        String rutaDestino = rutaRaiz + "WEB-INF" + separator + "about" + separator + "fotos";
        String[] cmd = new String[2];
        cmd[0] = fichero;
        cmd[1] = rutaDestino;

        Process f;
        try {
            f = Runtime.getRuntime().exec(cmd);
            try {
                f.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(SincronizeUserAction.class.getName()).log(Level.SEVERE, null, ex);
            }

            // retrieve output from python script
            BufferedReader bfr = new BufferedReader(new InputStreamReader(f.getInputStream()));
            String line;
            while ((line = bfr.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(SincronizeUserAction.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void guardaCopiaSeguridad(String rutaRaiz) {
        String separator = OS.getDirectorySeparator();
        String ruta_old_fotos = rutaRaiz + separator + "WEB-INF" + separator + "about"
                + separator + "old_fotos";
        File dir_old_fotos = new File(ruta_old_fotos);

        //Se comprueba si existe la carpeta, si existe se borra
        if (dir_old_fotos.exists()) {
            BorrarDirectorio.borrarDirectorio(dir_old_fotos);
        }

        //Se crea la carpeta
        dir_old_fotos.mkdir();

        //Cogemos todas fotos
        String ruta_fotos = rutaRaiz + separator + "WEB-INF" + separator + "about"
                + separator + "fotos";
        File fotos_current = new File(ruta_fotos);
        File[] ficheros = fotos_current.listFiles();
        for (File fichero : ficheros) {
            File copiaFoto = new File(ruta_old_fotos + separator + fichero.getName());
            FileCopy(fichero.getPath(), copiaFoto.getPath());

            fichero.delete();

        }

    }

    public void FileCopy(String sourceFile, String destinationFile) {
        System.out.println("Desde: " + sourceFile);
        System.out.println("Hacia: " + destinationFile);
        try {
            File inFile = new File(sourceFile);
            File outFile = new File(destinationFile);
            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Hubo un error de entrada/salida!!!");
        }
    }

    private void copiarFotos(String rutaRaiz) {
        String separator = OS.getDirectorySeparator();
        String rutaOrigen = rutaRaiz + "WEB-INF" + separator + "about" + separator + "fotos";
        String rutaDestino = rutaRaiz + "revista" + separator + "about" + separator + "fotos";
        
        File dir_origen = new File(rutaOrigen);
        File[]ficheros = dir_origen.listFiles();
        
        for (File fichero : ficheros) {
            File copiaFoto = new File(rutaDestino + separator + fichero.getName());
            FileCopy(fichero.getPath(), copiaFoto.getPath());
        }
    }
}
