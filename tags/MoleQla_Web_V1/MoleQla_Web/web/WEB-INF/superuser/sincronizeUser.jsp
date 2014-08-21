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
    String ubicacionRaiz = application.getRealPath(separator);
%>
<html:form action="/sincronizeUser" styleId="contact-sincronizaUser" styleClass="contact-sincronizaUser">                   
    <p><bean:write name="SincronizeUserActionForm" property="errorMsg" filter="false"/></p>
    <p><bean:write name="SincronizeUserActionForm" property="msg" filter="false"/></p>
    <html:hidden name="SincronizeUserActionForm" property="rutaRaiz" value="<%=ubicacionRaiz%>"/>
    <p class="contact-submit">
        <html:submit styleId="contact-submit-sincronizaUser" styleClass="submit" value="Synchronize">Synchronize</html:submit> 
        </p>
</html:form>                

