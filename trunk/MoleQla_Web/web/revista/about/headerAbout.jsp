<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<div class="sticky-nav">
    <jsp:include page="../header.html"/>

    <nav id="menu">
        <ul id="menu-nav">
            <li><a href="../inicio/inicio.jsp" class="external">Inicio</a></li>
            <li><html:link action="/revista/work/work" styleClass="external">Revistas</html:link></li>
            <li class="current"><a href="about.jsp" class="external">Equipo Editorial</a></li>
            <li><a href="../articulo/registrarAutor.jsp" class="external">Enviar Articulo</a></li>
            <li><a href="../contact/contact.jsp" class="external">Contactar</a></li>
        </ul>
    </nav>

</div>
