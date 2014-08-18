<%@page import="utilidades.OS"%>
<%
    String separator = OS.getDirectorySeparator();
    String ubicacionNumeros = application.getRealPath(separator + "WEB-INF" + separator + "numeros");
    String ubicacionRaiz = application.getRealPath(separator);

    request.setAttribute("ubicacionNumeros", ubicacionNumeros);
    request.setAttribute("ubicacionRaiz", ubicacionRaiz);
%>
<meta http-equiv="Refresh" content="0; url=revista/index/indexr.jsp">
