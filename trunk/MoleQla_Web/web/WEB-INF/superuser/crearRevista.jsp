<%-- 
    Document   : inicio
    Created on : 24-abr-2014, 12:06:16
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
        <%@include file="head.html" %>
    </head>
    <body>
        <header>
            <%@include file="headerRevista.jsp" %>
        </header>
        <div id="contact" class="page">

            <!-- Contact Form -->
            <div class="row">
                <div class="span9">
                    <%
                        String separator = OS.getDirectorySeparator();
                        String ubicacionNumeros = application.getRealPath(separator + "WEB-INF" + separator + "numeros");
                    %>
                    <html:form action="/WEB-INF/superuser/crearRevista" styleId="contact-new-revista" styleClass="contact-new-revista">                   
                        <p><bean:write name="CrearRevistaActionForm" property="errorMsg" filter="false"/></p>
                        <p><bean:write name="CrearRevistaActionForm" property="msg" filter="false"/></p>
                        <html:hidden name="CrearRevistaActionForm" property="rutaNumeros" value="<%=ubicacionNumeros%>"/>
                        <p class="contact-submit">
                            <html:submit styleId="contact-submit-new-revista" styleClass="submit" value="Create">Create</html:submit> 
                            </p>
                    </html:form>                

                </div>

            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
