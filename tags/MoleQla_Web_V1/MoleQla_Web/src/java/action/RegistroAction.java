/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.RegistroActionForm;
import connection.ConnectionPSQL;
import email.Mail;
import email.MailHtml;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utilidades.Constantes;
import utilidades.GenerarCadenaAlfanumerica;

/**
 *
 * @author Rafa
 */
public class RegistroAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private final static String CANCEL = "cancel";

    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

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
            // extract user data
            RegistroActionForm formBean = (RegistroActionForm) form;
            String nombre = formBean.getNombre();
            String apellido1 = formBean.getApellido1();
            String apellido2 = formBean.getApellido2();
            String email = formBean.getEmail();

            if (nombre.isEmpty() || apellido1.isEmpty() || email.isEmpty()) {
                formBean.setErrorMsg(Constantes.getERROR_FORM());
                return mapping.findForward(FAILURE);
            } else if (Mail.compruebaEmail(email) == false) {
                formBean.setErrorMsg(Constantes.getEMAIL_INCORRECT());
                return mapping.findForward(FAILURE);
            } else if (existeEmail(email)) {
                formBean.setErrorMsg(Constantes.getEMAIL_EXIST());
                return mapping.findForward(FAILURE);
            } else {
                String passOriginal = GenerarCadenaAlfanumerica.getCadena();
                //String passHash = GenerarCadenaAlfanumerica.generateStorngPasswordHash(passOriginal);

                try {
                    // Nombre UTF8
                    byte pnombre[] = nombre.getBytes(ISO_8859_1);
                    String nombreUTF8 = new String(pnombre, UTF_8);

                    // Apellido1 UTF8
                    byte papellido1[] = apellido1.getBytes(ISO_8859_1);
                    String apellido1UTF8 = new String(papellido1, UTF_8);

                    // Apellido2 UTF8
                    byte papellido2[] = apellido2.getBytes(ISO_8859_1);
                    String apellido2UTF8 = new String(papellido2, UTF_8);

                    // Nombre + Apellidos
                    String nombreCompleto = nombreUTF8 + " " + apellido1UTF8 + " " + apellido2UTF8;
                    // Valores timestamp
                    long milis = new java.util.GregorianCalendar().getTimeInMillis();
                    Timestamp fechaHora = new Timestamp(milis);

                    boolean error = false;

                    try (Connection connection = ConnectionPSQL.connection()) {

                        //Insert en la tabla res_partner
                        insertResPartner(connection, nombreUTF8, nombreCompleto, email, fechaHora);

                        //Update de la tabla res_partner
                        //updateResPartner(connection, email);
                        int partner_id = consultarIdUltResPartner(email);
                        if (partner_id > 0) {
                            //Insert en la tabla res_users
                            insertResUser(connection, email, passOriginal, partner_id, fechaHora);

                            int user_id = consultarIdUltResUsers(email);
                            if (user_id > 0) {
                                //Inser en la tabla res_company_users_rel
                                insertResCompanyUsersRel(connection, user_id);

                                //Insert en la tabla mail_alias
                                //insertMailAlias(connection, fechaHora, user_id);
                                //Update de la tabla res_users para añadirle el alias_id de mail_alias
                                //updateResUser_MailAlias(connection, email);
                                //Insert en la tabla res_groups_users_rel
                                insertResGroupUserRel(connection, user_id);

                                //Insert en la tabla autor
                                insertAutor(connection, user_id, fechaHora, nombreUTF8, apellido1UTF8 + " " + apellido2UTF8);

                            } else {
                                System.out.println("\n ERROR: select consultarIdMax(res_users)"
                                        + "\n-----------------\n"
                                        + Constantes.getERROR_SQL()
                                        + "\n-----------------\n"
                                        + "Email: " + email + "\n"
                                        + "Nombre: " + nombre + "\n"
                                        + "Apellido: " + apellido1 + "\n");
                                error = true;
                            }

                        } else {
                            System.out.println("\n ERROR: select consultarIdMax(res_partner)"
                                    + "\n-----------------\n"
                                    + Constantes.getERROR_SQL()
                                    + "\n-----------------\n"
                                    + "Email: " + email + "\n"
                                    + "Nombre: " + nombre + "\n"
                                    + "Apellido: " + apellido1 + "\n");
                            error = true;
                        }

                        if (error) {
                            connection.close();
                            formBean.setErrorMsg(Constantes.getERROR_SQL());
                            return mapping.findForward(FAILURE);
                        }
                        connection.close();
                    }
                    formBean.setMsg(Constantes.getREGISTRATION_OK());

                    String text = Constantes.getEMAIL_BIENVENIDA(email, nombreUTF8, apellido1UTF8, apellido2UTF8, passOriginal);
                    if (MailHtml.enviarMailHtml(email, text, Constantes.getEMAIL_ASUNTO()) == false) {
                        return mapping.findForward(FAILURE);
                    }

                } catch (SQLException e) {
                    formBean.setErrorMsg(Constantes.getERROR_SQL());
                    System.out.println("\n" + e.getMessage()
                            + "\n-----------------\n"
                            + Constantes.getERROR_SQL()
                            + "\n-----------------\n"
                            + "Email: " + email + "\n"
                            + "Nombre: " + nombre + "\n"
                            + "Apellido: " + apellido1 + "\n");
                    return mapping.findForward(FAILURE);
                }
            }

            return mapping.findForward(SUCCESS);
        }
    }

    public int consultarIdResGroups(String nombreGrupo) throws SQLException {
        int id = -1;
        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT id FROM res_groups WHERE name = '" + nombreGrupo + "'");

            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
            }

            connection.close();
        }
        return id;
    }

    public int consultarIdUltResUsers(String email) throws SQLException {
        int id = -1;
        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT id FROM res_users WHERE login = '" + email + "'");

            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
            }

            connection.close();
        }
        return id;
    }

    public int consultarIdUltResPartner(String email) throws SQLException {
        int id = -1;
        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT id FROM res_partner WHERE email = '" + email + "'");

            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
            }

            connection.close();
        }
        return id;
    }

    public int consultarIdUltMailAlias(int users_id) throws SQLException {
        int id = -1;
        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT id FROM mail_alias WHERE alias_parent_thread_id = '" + users_id + "'");

            if (rs != null) {

                while (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
            }

            connection.close();
        }
        return id;
    }

    public void insertResPartner(Connection connection, String nombreUTF8, String nombreCompleto, String email, Timestamp fechaHora) throws SQLException {
        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO res_partner("
                + "id, name, company_id, comment, ean13, create_date, color, image, "
                + "use_parent_address, active, street, supplier, city, user_id, "
                + "zip, title, function, country_id, parent_id, employee, type, "
                + "email, vat, website, lang, fax, create_uid, street2, phone, credit_limit, "
                + "write_date, date, tz, write_uid, display_name, customer, image_medium, "
                + "mobile, ref, image_small, birthdate, is_company, state_id, commercial_partner_id)"
                + "    VALUES (DEFAULT, '" + nombreUTF8 + "', 1, DEFAULT, DEFAULT, '" + fechaHora.toString() + "', 0, DEFAULT, "
                + "FALSE, TRUE, DEFAULT, FALSE, DEFAULT, DEFAULT, "
                + "DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, FALSE, 'contact', "
                + "'" + email + "', DEFAULT, DEFAULT, 'es_ES', DEFAULT, 1, DEFAULT, DEFAULT, 0, "
                + "'" + fechaHora.toString() + "', DEFAULT, DEFAULT, 1, '" + nombreCompleto + "', FALSE, DEFAULT, "
                + "DEFAULT, DEFAULT, DEFAULT, DEFAULT, FALSE, DEFAULT, DEFAULT);");

        connection.commit();

    }

    private void insertResUser(Connection connection, String email, String passOriginal, int partner_id, Timestamp fechaHora) throws SQLException {

        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO res_users("
                + "id, active, login, password, company_id, partner_id, create_date,"
                + "create_uid, write_uid, login_date, write_date, signature, action_id)"
                + " VALUES (DEFAULT, TRUE, '" + email + "', '" + passOriginal + "', 1, " + partner_id + ", '" + fechaHora.toString() + "',"
                + "1, 1, DEFAULT, '" + fechaHora.toString() + "', DEFAULT, DEFAULT);");

        connection.commit();

    }

    private void insertResGroupUserRel(Connection connection, int user_id) throws SQLException {

        int employee = consultarIdResGroups("Employee");
        int perfil = consultarIdResGroups("Perfil");
        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO res_groups_users_rel("
                + "gid, uid)"
                + " VALUES (" + employee + ", " + user_id + ");");

        boolean rsqa = connection.createStatement().execute("INSERT INTO res_groups_users_rel("
                + "gid, uid)"
                + " VALUES (" + perfil + ", " + user_id + ");");

        connection.commit();

    }

    private void insertAutor(Connection connection, int user_id, Timestamp fechaHora, String nombreUTF8, String apellidosUTF8) throws SQLException {
        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO autor("
                + "id, create_uid, create_date, user_id, write_uid, write_date, "
                + "nombre, apellidos)"
                + " VALUES (DEFAULT, 1, '" + fechaHora.toString() + "', '" + user_id + "',1, '" + fechaHora.toString() + "','" + nombreUTF8 + "','" + apellidosUTF8 + "');");

        connection.commit();

    }

    private void insertMailAlias(Connection connection, Timestamp fechaHora, int users_id) throws SQLException {
        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO mail_alias("
                + " id, create_uid, alias_parent_thread_id, write_uid, alias_defaults, "
                + "alias_contact, alias_parent_model_id, alias_user_id, alias_force_thread_id, "
                + "alias_model_id, write_date, create_date, alias_name)"
                + "    VALUES (DEFAULT, 1, " + users_id + ", 1, '{}', "
                + "'everyone', 92, 1, " + users_id + ", "
                + " 92, '" + fechaHora.toString() + "', '" + fechaHora.toString() + "', DEFAULT);");

        connection.commit();
    }

    private void insertResCompanyUsersRel(Connection connection, int user_id) throws SQLException {
        connection.setAutoCommit(false);

        boolean rsq = connection.createStatement().execute("INSERT INTO res_company_users_rel(\n"
                + "cid, user_id)\n"
                + "VALUES (1, " + user_id + ");");

        connection.commit();
    }

    /**
     * Para actualizar la columna alias_id
     *
     * @param connection
     * @throws SQLException
     */
    private void updateResUser_MailAlias(Connection connection, String email) throws SQLException {
        connection.setAutoCommit(false);

        int idUser = consultarIdUltResUsers(email);
        int idMailAlias = consultarIdUltMailAlias(idUser);

        int rsq = connection.createStatement().executeUpdate("UPDATE res_users"
                + "   SET alias_id=" + idMailAlias + ""
                + " WHERE id = " + idUser + ";");

        connection.commit();
    }

    /**
     * Para actualizar la columna commercial_partner_id
     *
     * @param connection
     * @throws SQLException
     */
    private void updateResPartner(Connection connection, String email) throws SQLException {
        connection.setAutoCommit(false);

        int idUserPartner = consultarIdUltResPartner(email);
        int rsq = connection.createStatement().executeUpdate("UPDATE res_partner"
                + "   SET commercial_partner_id= " + idUserPartner + ""
                + " WHERE id = " + idUserPartner + ";");

        connection.commit();
    }

    public boolean existeEmail(String email) throws SQLException {
        boolean existe = false;
        try (Connection connection = ConnectionPSQL.connection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT id FROM res_users WHERE login = '" + email + "'");

            boolean error = false;
            if (rs != null) {
                String id = "";
                while (rs.next()) {
                    id = rs.getString(1);
                }
                if (id.isEmpty() == false) {
                    existe = true;
                }
                rs.close();
            }

            connection.close();
        }
        return existe;
    }

}
