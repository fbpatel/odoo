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
        <%@include file="headAcciones.html" %>
    </head>
    <body>
        <header>
            <%@include file="headerAcciones.jsp" %>
        </header>
        <div id="contact" class="page">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Zona de Administracion</h2>
                    </div>
                </div>
                <!-- Contact Form -->
                <div class="row">
                    <div class="span9">
                        <html:form styleId="accion-crear" action="/acciones">
                            <p><bean:write name="AccionesActionForm" property="errorMsg" filter="false"/></p>
                            <html:submit value="Sincronizar los numeros" styleClass="submit"/>
                            <html:hidden value="crearRevista" property="opcion" />
                        </html:form>
                            
                        <html:form styleId="accion-sincronizar" action="/acciones">
                            <p><bean:write name="AccionesActionForm" property="errorMsg" filter="false"/></p>
                            <html:submit value="Sincronizar los usuarios" styleClass="submit"/> 
                            <html:hidden value="sincronizar" property="opcion" />
                        </html:form>
                    </div>

                </div>
                <!-- End Contact Form -->
            </div>
        </div>
    </body>
</html>