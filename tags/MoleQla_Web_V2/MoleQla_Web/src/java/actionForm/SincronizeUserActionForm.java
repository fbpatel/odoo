/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author Rafa
 */
public class SincronizeUserActionForm extends org.apache.struts.action.ActionForm {

    private String error;
    private String errorMsg;
    private String msg;
    private String rutaRaiz;

    public String getRutaRaiz() {
        return rutaRaiz;
    }

    public void setRutaRaiz(String rutaRaiz) {
        this.rutaRaiz = rutaRaiz;
    }    

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = "<span style='color:green'>"+msg+"</span>";
    }    
    
    public String getError() {
        return error;
    }

    public void setError() {
        this.error
                = "<span style='color:red'>Error</span>";
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = "<span style='color:red'>"+errorMsg+"</span>";
    }      
    
    /**
     *
     */
    public SincronizeUserActionForm() {
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
