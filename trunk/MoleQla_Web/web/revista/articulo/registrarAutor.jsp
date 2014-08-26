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
        <script type="text/javascript">
            $(function() {

                $("#name_autor").attr("placeholder", "Name *");
                $("#surname1_autor").attr("placeholder", "First Surname *");
                $("#surname2_autor").attr("placeholder", "Second Surname");
                $("#email_autor").attr("placeholder", "Email *");

            });

        </script>
    </head>
    <body>
        <%@include file="../circulo.html" %>

        <header>
            <%@include file="headerAutor.jsp" %>
        </header>
        <div id="contact" class="page">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Registrate en MoleQla</h2>
                        <h3 class="title-description">Registrandote en MoleQla podr&aacute;s enviarnos todos los art&iacute;culos que desees</h3>
                    </div>
                </div>
                <!-- Contact Form -->
                <div class="row">
                    <div class="span9">

                        <html:form action="/revista/articulo/registrarAutor" styleId="contact-register" styleClass="contact-register">                   
                            <p><bean:write name="RegistroActionForm" property="errorMsg" filter="false"/></p>
                            <p><bean:write name="RegistroActionForm" property="msg" filter="false"/></p>

                            <p class="contact-name"><html:text name="RegistroActionForm" styleId="name_autor" property="nombre"/> </p>
                            <p class="contact-surname1"><html:text name="RegistroActionForm" styleId="surname1_autor" property="apellido1"/></p>
                            <p class="contact-surname2"><html:text name="RegistroActionForm" styleId="surname2_autor" property="apellido2"/></p>
                            <p class="contact-email"><html:text name="RegistroActionForm" styleId="email_autor" property="email"/></p>

                            <p class="contact-submit">
                                <html:submit styleId="contact-submit-register" styleClass="submit" value="Register">Register</html:submit> 
                                <html:cancel styleId="contact-cancel-register" styleClass="submit" value="Cancel">Cancel</html:cancel>
                                </p>
                        </html:form> 

                    </div>

                </div>
            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
