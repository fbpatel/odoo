/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actionForm;

import java.io.File;
import java.util.List;

/**
 *
 * @author Rafa
 */
public class MostrarNumerosActionForm extends org.apache.struts.action.ActionForm {

    private String error;
    private String errorMsg;
    private String msg;
    private String rutaNumeros;
    private List<File> listaNumerosPublicados;

    public List<File> getListaNumerosPublicados() {
        return listaNumerosPublicados;
    }

    public void setListaNumerosPublicados(List<File> listaNumerosPublicados) {
        this.listaNumerosPublicados = listaNumerosPublicados;
    }

    
    public String getRutaNumeros() {
        return rutaNumeros;
    }

    public void setRutaNumeros(String rutaNumeros) {
        this.rutaNumeros = rutaNumeros;
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
    public MostrarNumerosActionForm() {
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
