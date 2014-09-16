<%-- 
    Document   : work
    Created on : 26-abr-2014, 10:52:47
    Author     : Rafa
--%>

<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
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
        <style>
            .span9 {
                width: 100%;
                text-align: center; 
                color: #000;
            }

            #projects{
                margin-left: 5%;
                margin-top: 10%;
            }

        </style>
    </head>
    <body>
        <%

            List<File> listaNumerosPublicados = (List<File>) request.getAttribute("listaNumerosPublicados");

            String nameServer = request.getContextPath();
        %>
        <%@include file="../circulo.html" %>

        <header>
            <%@include file="headerWork.jsp" %>
        </header>
        <div class="page-alternate">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Issues</h2>
                        <h3 class="title-description">In this section you will find all the issues published in the journal</h3>
                    </div>
                </div>
                <!-- Comienzo de la galeria de numeros -->
                <div class="span9">
                    <div class="row">
                        <section id="projects">

                            <%
                                // A continuación, se va a ordenador la lista por orden de numero
                                Collections.sort(listaNumerosPublicados, new Comparator<File>() {
                                    @Override
                                    public int compare(File u1, File u2) {
                                        //cadena.substring(0, cadena.length()-1); 
                                        Integer uu1 = Integer.parseInt(u1.getName().substring(0, u1.getName().length() - 4));
                                        Integer uu2 = Integer.parseInt(u2.getName().substring(0, u2.getName().length() - 4));

                                        //return uu1.compareTo(uu2);//de menos a mayor
                                        return uu2.compareTo(uu1);//de mayor a menor
                                    }
                                });

                                int i = 0, j;

                                while (i < listaNumerosPublicados.size()) {
                            %>            
                            <ul id="thumbs">
                                <%
                                    j = 0;
                                    while (j < 4 && i < listaNumerosPublicados.size()) {
                                        String name = listaNumerosPublicados.get(i).getName();
                                        String num = name.substring(0, name.length() - 4);
                                %>
                                <!-- Item Project and Filter Name -->                        
                                <li class="item-thumbs span3 design">  
                                    <b style="color: white">Issue <%=num%></b>
                                    <a href="<%=nameServer + "/revista/work/" + listaNumerosPublicados.get(i).getName()%>" target="_blank">
                                        <img src="../../_include/img/pdf.jpg" width="100" height="200" title="N&uacute;mero <%=num%>"/>
                                    </a>
                                </li>
                                <!-- End Item Project -->

                                <%
                                        i++;
                                        j++;

                                    }
                                %>
                            </ul>
                            <%
                                }
                            %>
                        </section>
                    </div>
                </div>
            </div>
        </div>
        <!-- End Galeria -->
    </body>
</html>
