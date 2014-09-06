/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actionForm;

import action.User;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author Rafa
 */
public class AccionesActionForm extends org.apache.struts.action.ActionForm {

    private String opcion;
    private String errorMsg;

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = "<span style='color:red'>" + errorMsg + "</span>";
    }

    /**
     *
     */
    public AccionesActionForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    /*public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
     ActionErrors errors = new ActionErrors();
     if (getUser() == null || getUser().length() < 1) {
     errors.add("user", new ActionMessage("error.user.required"));
     // TODO: add 'error.name.required' key to your resources
     }
     return errors;
     }*/
}
