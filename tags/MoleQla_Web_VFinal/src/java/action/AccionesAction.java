/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.AboutActionForm;
import actionForm.AccionesActionForm;
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
public class AccionesAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String REVISTA = "crear";
    private final static String SINCRONIZA = "sincronizar";

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

        AccionesActionForm formBean = (AccionesActionForm)form;
        String opc = formBean.getOpcion();
        
        String res = "failure";
        switch (opc) {
            case "crearRevista":
                res = REVISTA;
                break;
            case "sincronizar":
                res = SINCRONIZA;
                break;
        }
        
        formBean.setErrorMsg("ERROR: en la redireccion");
        return mapping.findForward(res);
    }

}
