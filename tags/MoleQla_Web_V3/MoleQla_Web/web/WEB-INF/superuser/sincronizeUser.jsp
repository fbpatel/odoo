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
        <%
            String separator = OS.getDirectorySeparator();
            String ubicacionRaiz = application.getRealPath(separator);
        %>
        <header>
            <%@include file="headerRevista.jsp" %>
        </header>
        <div class="page-alternate">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Sincronizaci&oacute;n de usuarios</h2>
                    </div>
                </div>
                <html:form action="/sincronizeUser" styleId="contact-sincronizaUser" styleClass="contact-sincronizaUser">                   
                    <p><bean:write name="SincronizeUserActionForm" property="errorMsg" filter="false"/></p>
                    <p><bean:write name="SincronizeUserActionForm" property="msg" filter="false"/></p>
                    <html:hidden name="SincronizeUserActionForm" property="rutaRaiz" value="<%=ubicacionRaiz%>"/>
                    <p class="contact-submit">
                        <html:submit styleId="contact-submit-sincronizaUser" styleClass="submit" value="Synchronize">Synchronize</html:submit> 
                        </p>
                </html:form>                

            </div>
        </div>

    </body>
</html>