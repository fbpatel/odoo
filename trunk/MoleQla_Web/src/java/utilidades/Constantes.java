/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

/**
 *
 * @author Rafa
 */
public class Constantes {

    //Estas variables se corresponden con las de la base de datos
    //Contantes mensajes
    private static final String REGISTRATION_OK = "Successful registration. Sent you an email with the details of your registration MoleQla";
    private static final String CREACION_NUMERO_OK = "Successful create.";

    //Constante para el error
    private static final String ERROR_FORM = "All field are required";
    private static final String ERROR_LOGIN = "User and/or password incorrect";

    private static final String ERROR_FORM_ADD = "Error en la inserseccion";
    private static final String ERROR_CREACION_REVISTA = "Error al crear la revista";
    private static final String ERROR_SQL = "Error: Try again";

    //Constante para el email
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String EMAIL_INCORRECT = "Incorrect email";
    private static final String EMAIL_ERROR = "Error al enviar el correo";
    private static final String EMAIL_EXIST = "This email already exists";

    private static String EMAIL_BIENVENIDA;
    private static final String EMAIL_ASUNTO = "Welcome to MoleQla";
    private static final String EMAIL_NOTIFICA = "moleqlanotify@gmail.com";
    private static final String EMAIL_NOTIFICA_PASS = "etwr80notifica";
    private static final String EMAIL_URL_LOGO = "http://www.upo.es/moleqla/export/system/modules/es.upo.moleqla.aquigar/resources/images/logos_seccion/logo1.jpg";
    
    // Nuevo numero
    private static final String ESTADO_NUMEROS_PUBLICAR = "a_publicar";
    private static final String ERROR_CREAR_NUMERO = "ERROR: Try again";
    
    private static String SEPARATOR;

    public static String getERROR_LOGIN() {
        return ERROR_LOGIN;
    }    
    
    public static String getERROR_CREAR_NUMERO() {
        return ERROR_CREAR_NUMERO;
    }    

    public static String getCREACION_NUMERO_OK() {
        return CREACION_NUMERO_OK;
    }   
    
    
    /**
     * Estado para obtener los numeros aun no publicados, pero que ya se pueden publicar
     * @return 
     */
    public static String getESTADO_NUMEROS_PUBLICAR() {
        return ESTADO_NUMEROS_PUBLICAR;
    }    
    
    /**
     * URL del logo que va insertado en el correo
     * @return 
     */
    public static String getEMAIL_URL_LOGO() {
        return EMAIL_URL_LOGO;
    }
    
    public static String getEMAIL_NOTIFICA() {
        return EMAIL_NOTIFICA;
    }

    public static String getEMAIL_NOTIFICA_PASS() {
        return EMAIL_NOTIFICA_PASS;
    }
 
    
    /**
     * Mensaje de que ya existe ese email
     * @return 
     */
    public static String getEMAIL_EXIST() {
        return EMAIL_EXIST;
    }

    
    
    /**
     * Log de error al enviar el correo
     *
     * @return
     */
    public static String getEMAIL_ERROR() {
        return EMAIL_ERROR;
    }

    /**
     * Asunto del email de registro
     *
     * @return
     */
    public static String getEMAIL_ASUNTO() {
        return EMAIL_ASUNTO;
    }

    /**
     * Este será el cuperpo del email al registrarse un nuevo autor
     *
     * @param nombre
     * @param apellido1
     * @return
     */
    public static String getEMAIL_BIENVENIDA(String email,String nombre, String apellido1, String apellido2, String pass) {
        String cad = "";
        if (apellido2.isEmpty()) {
            cad = "Welcome to MoleQla <b>" + nombre + " " + apellido1 + "</b>. <br />"
                    + "Your data are:"
                    + "<li> Email: " + email + "</li>"
                    + "<li> Name: " + nombre + "</li>"
                    + "<li> First surname: " + apellido1 + "</li>"
                    + "<li> Password: " + pass + "</li>"
                    + "<br />If you wish to change your data...";
        } else {
            cad = "Welcome to MoleQla <b>" + nombre + " " + apellido1 + " " + apellido2 + "</b>. <br />"
                    + "Your data are:"
                    + "<li> Email: " + email + "</li>"
                    + "<li> Name: " + nombre + "</li>"
                    + "<li> First surname: " + apellido1 + "</li>"
                    + "<li> Second surname: " + apellido2 + "</li>"
                    + "<li> Password: " + pass + "</li>"
                    + "<br />If you wish to change your data...";
        }
        
        cad += "<br />Go to this url and login with your credentials <a href=\"www.openerp.com\">Odoo</a>";
        return cad;
    }

    /**
     * Error para que se completen todos los campos de un formulario
     *
     * @return
     */
    public static String getERROR_FORM() {
        return ERROR_FORM;
    }

    /**
     * Error al anadir algo
     *
     * @return
     */
    public static String getERROR_FORM_ADD() {
        return ERROR_FORM_ADD;
    }

    /**
     * Metodo para saber en que sistema operativo esta
     *
     * @return
     */
    public static String getSEPARATOR() {
        SEPARATOR = OS.getDirectorySeparator();
        return SEPARATOR;
    }

    /**
     * Expresion regular que comprueba un email
     *
     * @return
     */
    public static String getPATTERN_EMAIL() {
        return PATTERN_EMAIL;
    }

    /**
     * Error al publicar una revista
     *
     * @return
     */
    public static String getERROR_CREACION_REVISTA() {
        return ERROR_CREACION_REVISTA;
    }

    /**
     * Mensaje de ok al registrarse
     *
     * @return
     */
    public static String getREGISTRATION_OK() {
        return REGISTRATION_OK;
    }

    /**
     * Mensaje ed email incorrecto
     *
     * @return
     */
    public static String getEMAIL_INCORRECT() {
        return EMAIL_INCORRECT;
    }

    /**
     * Mensaje de error al insertar
     *
     * @return
     */
    public static String getERROR_SQL() {
        return ERROR_SQL;
    }

}
