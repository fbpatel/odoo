<%-- 
    Document   : work
    Created on : 26-abr-2014, 10:52:47
    Author     : Rafa
--%>

<%@page import="utilidades.OS"%>
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
        <style type="text/css">
            #bljaIMGte{
                float:left;position:relative;
            }
            #bljaIMGte .bljaIMGtex {
                width:320px;
                position:absolute;
                top:60px;
                left:25px;
                font-size: 80px
            }
        </style>
    </head>
    <body>
        <%@include file="../circulo.html" %>

        <header>
            <%@include file="headerWork.jsp" %>
        </header>
        <%

            String separator = OS.getDirectorySeparator();
            String ubicacionNumeros = application.getRealPath(separator + "WEB-INF" + separator + "numeros");

            session.setAttribute("ubicacionNumeros", ubicacionNumeros);
        %>


        <a id="bljaIMGte" href="http://www.marca.com">
            <img src="../../_include/img/pdf.jpg" width="150" height="300"/>
            <div class="bljaIMGtex" style="color:#000000;">
                <p>14</p>
            </div>
        </a>

        <div>
            <embed scale="tofit" name="PDFEmbed" alt="" height="554" id="PDFEmbedID" 
                   src="pdfimagen.pdf" 
                   type="application/pdf" controller="true" width="100%">
        </div>
    </body>
</html>
