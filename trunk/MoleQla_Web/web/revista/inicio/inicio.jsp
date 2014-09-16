<%-- 
    Document   : inicio
    Created on : 24-abr-2014, 12:06:16
    Author     : Rafa
--%>
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
            <%@include file="headerInicio.jsp" %>
        </header>

        <div id="inicio" class="page-alternate">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">MoleQla</h2>
                        <h3 class="title-description">Welcome to the Journal of Science of the University Pablo de Olavide</h3>
                    </div>
                </div>
                <jsp:include page="inicio.html"/>
            </div>
        </div>

    </body>
</html>
