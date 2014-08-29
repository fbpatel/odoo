/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static action.RegistroAction.ISO_8859_1;
import static action.RegistroAction.UTF_8;
import actionForm.ContactActionForm;
import actionForm.LoginActionForm;
import connection.ConnectionPSQL;
import email.Mail;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utilidades.Constantes;

/**
 *
 * @author Rafa
 */
public class ContactAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCES = "success";
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
        boolean error = true;
        if (isCancelled(request)) {
            return mapping.findForward(CANCEL);
        } else {
            // extract user data
            ContactActionForm formBean = (ContactActionForm) form;
            String name = formBean.getName();
            String email = formBean.getEmail();
            String message = formBean.getMessage();

            // Se codifican las variables para que en la BD se guarden bien
            //Nombre
            byte pname[] = name.getBytes(ISO_8859_1);
            String nameUTF8 = new String(pname, UTF_8);

            //Mensaje
            byte pnessage[] = message.getBytes(ISO_8859_1);
            String messageUTF8 = new String(pnessage, UTF_8);
            
            //Email
            byte pemail[] = email.getBytes(ISO_8859_1);
            String emailUTF8 = new String(pemail, UTF_8);

            //Se ponen las mismas variables que hemos recogido pero codificadas para que en la web salgan bien
            formBean.setName(nameUTF8);
            formBean.setMessage(messageUTF8);
            formBean.setEmail(emailUTF8);
            if (nameUTF8.isEmpty() || emailUTF8.isEmpty() || messageUTF8.isEmpty()) {
                
                formBean.setErrorMsg(Constantes.getERROR_FORM_CONTACT());
                return mapping.findForward(FAILURE);
            } else if (Mail.compruebaEmail(emailUTF8)==false) {
                formBean.setErrorMsg(Constantes.getEMAIL_INCORRECT());
                return mapping.findForward(FAILURE);
            } else {

                String cuerpo = Constantes.getEMAIL_TEXTO_CONTACT(emailUTF8, messageUTF8);
                String asunto = "Interesado " + nameUTF8;
                String emailMoleqLa = Constantes.getEMAIL_NOTIFICA();
                error = Mail.enviarMail(emailMoleqLa, cuerpo, asunto);
            }

            if (error == false) {
                formBean.setErrorMsg(Constantes.getERROR_FORM_CONTACT_EMAIL());
                return mapping.findForward(FAILURE);
            }

            formBean.setMsg(Constantes.getEMAIL_CONTACT_OK());
            return mapping.findForward(SUCCES);
        }
    }
}
