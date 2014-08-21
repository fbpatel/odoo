/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.AboutActionForm;
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
public class AboutAction extends org.apache.struts.action.Action {

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

        AboutActionForm formBean = (AboutActionForm) form;
        String rutaRaiz = request.getServletContext().getRealPath("/");

        List<User> listaDatosUserEditor = consultaDatosUserEditor(rutaRaiz);
        formBean.setListaUsuariosEditor(listaDatosUserEditor);
        request.setAttribute("listaDatosUserEditor", listaDatosUserEditor);

        List<User> listaDatosUserMaquetador = consultaDatosUserMaquetador(rutaRaiz);
        formBean.setListaUsuariosMaquetador(listaDatosUserMaquetador);
        request.setAttribute("listaDatosUserMaquetador", listaDatosUserMaquetador);

        return mapping.findForward(SUCCESS);
    }

    private List<User> consultaDatosUserMaquetador(String rutaRaiz) {
        List<User> listaUsuarios = new ArrayList();
        String separator = OS.getDirectorySeparator();
        String ruta_fotos = rutaRaiz + separator + "WEB-INF" + separator + "about"
                + separator + "fotos";

        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT rp.id, e.nombre, e.descripcion FROM maquetador e INNER JOIN res_users ru ON e.user_id = ru.id LEFT JOIN res_partner rp ON ru.partner_id = rp.id");

            int id = 0;
            String nombre = "", descripcion = "";
            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                    nombre = rs.getString(2);
                    descripcion = rs.getString(3);

                    File foto = new File(ruta_fotos + separator + id + ".jpg");
                    if (foto.exists()) {
                        User us = new User();
                        us.setId(id);
                        us.setNombre(nombre);
                        us.setDescripcion(descripcion);
                        us.setFoto(foto);

                        listaUsuarios.add(us);
                    }
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AboutAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaUsuarios;
    }

    private List<User> consultaDatosUserEditor(String rutaRaiz) {
        List<User> listaUsuarios = new ArrayList();
        String separator = OS.getDirectorySeparator();
        String ruta_fotos = rutaRaiz + separator + "WEB-INF" + separator + "about"
                + separator + "fotos";

        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT rp.id, e.nombre, e.descripcion FROM editor e INNER JOIN res_users ru ON e.user_id = ru.id LEFT JOIN res_partner rp ON ru.partner_id = rp.id");

            int id = 0;
            String nombre = "", descripcion = "";
            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                    nombre = rs.getString(2);
                    descripcion = rs.getString(3);

                    File foto = new File(ruta_fotos + separator + id + ".jpg");
                    if (foto.exists()) {
                        User us = new User();
                        us.setId(id);
                        us.setNombre(nombre);
                        us.setDescripcion(descripcion);
                        us.setFoto(foto);

                        listaUsuarios.add(us);
                    }
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AboutAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaUsuarios;
    }
}
