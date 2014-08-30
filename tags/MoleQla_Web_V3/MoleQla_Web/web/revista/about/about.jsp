<%-- 
    Document   : about
    Created on : 26-abr-2014, 10:52:21
    Author     : Rafa
--%>

<%@page import="action.User"%>
<%@page import="java.util.List"%>
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
        <%

            List<User> listaDatosUserEditorJefe = (List<User>) request.getAttribute("listaDatosUserEditorJefe");
            List<User> listaDatosUserEditor = (List<User>) request.getAttribute("listaDatosUserEditor");
            List<User> listaDatosUserMaquetador = (List<User>) request.getAttribute("listaDatosUserMaquetador");
            String nameServer = request.getContextPath();
        %>
        <%@include file="../circulo.html" %>
        <header>
            <%@include file="headerAbout.jsp" %>
        </header>
        <div id="about" class="page-alternate">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Equipo Editorial</h2>
                        <h3 class="title-description">Este es nuestro equipo editorial</h3>
                    </div>
                </div>


                <!-- Comienzo de la galeria de user -->

                <div class="row">
                    <!-- Editores Jefe -->
                    <% for (int i = 0; i < listaDatosUserEditorJefe.size(); i++) {%>
                    <!-- Start Profile -->
                    <div class="span4 profile">
                        <div class="image-wrap">
                            <div class="hover-wrap">
                                <span class="overlay-img"></span>
                                <span class="overlay-text-thumb">Editor Jefe</span>
                            </div>
                            <img src="<%=nameServer + "/revista/about/fotos/" + listaDatosUserEditorJefe.get(i).getFoto().getName()%>" alt="<%=listaDatosUserEditorJefe.get(i).getNombre()%>"
                                 style="width: 370px !important; height: 270px !important">
                        </div>
                        <h3 class="profile-name"><%=listaDatosUserEditorJefe.get(i).getNombre()%></h3>
                        <p class="profile-description"><%=listaDatosUserEditorJefe.get(i).getDescripcion()%></p>

                    </div>
                    <!-- End Profile -->  
                    <% }%>
                </div>

                <div class="row">
                    <!-- Editores -->
                    <% for (int i = 0; i < listaDatosUserEditor.size(); i++) {%>
                    <!-- Start Profile -->
                    <div class="span4 profile">
                        <div class="image-wrap">
                            <div class="hover-wrap">
                                <span class="overlay-img"></span>
                                <span class="overlay-text-thumb">Editor de Seccion</span>
                            </div>
                            <img src="<%=nameServer + "/revista/about/fotos/" + listaDatosUserEditor.get(i).getFoto().getName()%>" alt="<%=listaDatosUserEditor.get(i).getNombre()%>"
                                 style="width: 370px !important; height: 270px !important">
                        </div>
                        <h3 class="profile-name"><%=listaDatosUserEditor.get(i).getNombre()%></h3>
                        <p class="profile-description"><%=listaDatosUserEditor.get(i).getDescripcion()%></p>

                    </div>
                    <!-- End Profile -->  
                    <% }%>
                </div>

                <div class="row">
                    <!-- Maquetadores -->
                    <% for (int i = 0; i < listaDatosUserMaquetador.size(); i++) {%>
                    <!-- Start Profile -->
                    <div class="span4 profile">
                        <div class="image-wrap">
                            <div class="hover-wrap">
                                <span class="overlay-img"></span>
                                <span class="overlay-text-thumb">Maquetador de Seccion</span>
                            </div>
                            <img src="<%=nameServer + "/revista/about/fotos/" + listaDatosUserMaquetador.get(i).getFoto().getName()%>" alt="<%=listaDatosUserMaquetador.get(i).getNombre()%>"
                                 style="width: 370px !important; height: 270px !important">
                        </div>
                        <h3 class="profile-name"><%=listaDatosUserMaquetador.get(i).getNombre()%></h3>
                        <p class="profile-description"><%=listaDatosUserMaquetador.get(i).getDescripcion()%></p>

                    </div>
                    <!-- End Profile -->  
                    <% }%>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
