/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.MostrarNumerosActionForm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import utilidades.OS;

/**
 *
 * @author Rafa
 */
public class MostrarNumerosAction extends org.apache.struts.action.Action {

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
            MostrarNumerosActionForm formBean = (MostrarNumerosActionForm) form;
            String rutaRaiz = request.getServletContext().getRealPath("/");            
            
            List<File> listaNumerosPublicados = consultaNumeros(rutaRaiz);
            formBean.setListaNumerosPublicados(listaNumerosPublicados);
            request.setAttribute("listaNumerosPublicados", listaNumerosPublicados);

            formBean.setMsg(Constantes.getCREACION_NUMERO_OK());
            return mapping.findForward(SUCCESS);
        }
    }

    public List<File> consultaNumeros(String rutaRaiz) {
        //Obtenemos la ruta donde estan los numeros creados
        String separator = OS.getDirectorySeparator();
        String rutaWork = rutaRaiz + "revista" + separator + "work";        
        File f = new File(rutaWork);
        
        //Se añaden todos los numeros a la lista que se va a retornar
        List<File> listaNumeros = new ArrayList();
        if (f.exists()) { // Directorio existe 

            File[] ficheros = f.listFiles();
            for (File fichero : ficheros) {
                if (fichero.getName().equals("headerWork.jsp") == false &&
                        fichero.getName().equals("work.jsp") == false) {
                    listaNumeros.add(fichero);
                }
            }

        }
        return listaNumeros;
    }
}
