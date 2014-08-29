<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<!DOCTYPE html>
<head>
    <%@include file="../head.html" %>
</head>
<body>
    <!-- This section is for Splash Screen -->
    <%@include file="../circulo.html" %>
    <!-- End of Splash Screen -->
    
    <!-- Homepage Slider -->
    <div id="home-slider">	
        <div class="overlay"></div>

        <div class="slider-text">
            <div id="slidecaption"></div>
        </div>   

        <div class="control-nav">
            <a id="prevslide" class="load-item"><i class="font-icon-arrow-simple-left"></i></a>
            <a id="nextslide" class="load-item"><i class="font-icon-arrow-simple-right"></i></a>
            <ul id="slide-list"></ul>

            <a id="nextsection" href="../inicio/inicio.jsp"><i class="font-icon-arrow-simple-down"></i></a>
        </div>
    </div>
    <!-- End Homepage Slider -->    
</body>