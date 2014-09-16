/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import actionForm.LoginActionForm;
import connection.ConnectionPSQL;
import java.io.IOException;
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
public class LoginAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUPERUSER = "superuser";
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
        boolean error;
        if (isCancelled(request)) {
            return mapping.findForward(CANCEL);
        } else {
            // extract user data
            LoginActionForm formBean = (LoginActionForm) form;
            String user = formBean.getUser();
            String password = formBean.getPassword();

            if (user.isEmpty() || password.isEmpty()) {
                formBean.setErrorMsg(Constantes.getERROR_LOGIN());
                return mapping.findForward(FAILURE);
            } else {

                if (Constantes.getEMAIL_ADMINISTRATION(user)) {                    
                    error = Constantes.getPASSWORD_ADMINISTRATION(user, password);
                } else {
                    error = false;
                }
            }

            if (error == false) {
                formBean.setErrorMsg(Constantes.getERROR_LOGIN());
                return mapping.findForward(FAILURE);
            }

            return mapping.findForward(SUPERUSER);
        }
    }
}
