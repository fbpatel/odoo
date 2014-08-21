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
public class AboutActionForm extends org.apache.struts.action.ActionForm {

    private String error;
    private String errorMsg;
    private String msg;
    private String rutaFotos;
    private List<User> listaUsuariosEditor;
    private List<User> listaUsuariosMaquetador;

    public List<User> getListaUsuariosEditor() {
        return listaUsuariosEditor;
    }

    public void setListaUsuariosEditor(List<User> listaUsuariosEditor) {
        this.listaUsuariosEditor = listaUsuariosEditor;
    }

    public List<User> getListaUsuariosMaquetador() {
        return listaUsuariosMaquetador;
    }

    public void setListaUsuariosMaquetador(List<User> listaUsuariosMaquetador) {
        this.listaUsuariosMaquetador = listaUsuariosMaquetador;
    }

    
    public String getRutaFotos() {
        return rutaFotos;
    }

    public void setRutaFotos(String rutaFotos) {
        this.rutaFotos = rutaFotos;
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
    public AboutActionForm() {
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
