<%-- 
    Document   : inicio
    Created on : 24-abr-2014, 12:06:16
    Author     : Rafa
--%>
<%@page import="utilidades.OS"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String separator = OS.getDirectorySeparator();
    String ubicacionWEBINF = application.getRealPath(separator + "WEB-INF");
    String ubicacionRaiz = application.getRealPath(separator);

%>
<html:form action="/crearRevista" styleId="contact-new-revista" styleClass="contact-new-revista">                   
    <p><bean:write name="CrearRevistaActionForm" property="errorMsg" filter="false"/></p>
    <p><bean:write name="CrearRevistaActionForm" property="msg" filter="false"/></p>
    <html:hidden name="CrearRevistaActionForm" property="rutaNumeros" value="<%=ubicacionWEBINF%>"/>
    <html:hidden name="CrearRevistaActionForm" property="rutaRaiz" value="<%=ubicacionRaiz%>"/>
    <p class="contact-submit">
        <html:submit styleId="contact-submit-new-revista" styleClass="submit" value="Create">Create</html:submit> 
        </p>
</html:form>                

