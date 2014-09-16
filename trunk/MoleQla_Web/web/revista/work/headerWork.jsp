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
            <li><a href="../inicio/inicio.jsp" class="external">Home</a></li>
            <li class="current"><html:link action="/revista/work/work" styleClass="external">Issues</html:link></li>
            <li><html:link action="/revista/about/about" styleClass="external">Editorial Team</html:link></li>
            <li><a href="../articulo/registrarAutor.jsp" class="external">Sign In</a></li>
            <li><a href="../contact/contact.jsp" class="external">Contact</a></li>
        </ul>
    </nav>

</div>
