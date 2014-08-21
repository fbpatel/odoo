<%-- 
    Document   : cotact
    Created on : 26-abr-2014, 10:52:36
    Author     : Rafa
--%>
<%@page import="utilidades.OS"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if (IE 7)&!(IEMobile)]><html class="no-js lt-ie9 lt-ie8" lang="en"><![endif]-->
<!--[if (IE 8)&!(IEMobile)]><html class="no-js lt-ie9" lang="en"><![endif]-->
<!--[if (IE 9)]><html class="no-js ie9" lang="en"><![endif]-->
<!--[if gt IE 8]><!--> 
<html lang="en-US"> <!--<![endif]-->
    <head>
        <%@include file="../head.html" %>
    </head>
    <body>
        <%@include file="../circulo.html" %>

        <header>
            <%@include file="headerAutor.jsp" %>
        </header>
        <div id="contact" class="page">

            <!-- Contact Form -->
            <div class="row">
                <div class="span9">
                    
                    <html:form action="/revista/articulo/registrarAutor" styleId="contact-register" styleClass="contact-register">                   
                        <p><bean:write name="RegistroActionForm" property="errorMsg" filter="false"/></p>
                        <p><bean:write name="RegistroActionForm" property="msg" filter="false"/></p>

                        <p class="contact-name">Name *: <html:text name="RegistroActionForm" property="nombre"/> </p>
                        <p class="contact-surname1">First surname *: <html:text name="RegistroActionForm" property="apellido1"/></p>
                        <p class="contact-surname2">Second surname : <html:text name="RegistroActionForm" property="apellido2"/></p>
                        <p class="contact-email">Email *: <html:text name="RegistroActionForm" property="email"/></p>
                        
                        <p class="contact-submit">
                            <html:submit styleId="contact-submit-register" styleClass="submit" value="Register">Register</html:submit> 
                            <html:cancel styleId="contact-cancel-register" styleClass="submit" value="Cancel">Cancel</html:cancel>
                            </p>
                    </html:form> 

                </div>

            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
