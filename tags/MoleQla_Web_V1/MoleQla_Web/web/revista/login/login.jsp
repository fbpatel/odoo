<%-- 
    Document   : login
    Created on : 24-abr-2014, 11:59:31
    Author     : Rafa
--%>
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
        <%@include file="../head.html" %>
    </head>
    <body>
        <%@include file="../circulo.html" %>

        <header>
             <%@include file="headerLogin.jsp" %>            
        </header>
        <div id="contact" class="page">

            <!-- Contact Form -->
            <div class="row">
                <div class="span9">
                    <html:form action="/revista/login/login" styleId="contact-login" styleClass="contact-login">
                        <p><bean:write name="LoginActionForm" property="errorMsg" filter="false"/></p>
                        <p class="contact-login-user">Enter your name:<html:text property="user" /></p>
                        <p class="contact-login-password">Enter your password:<html:password property="password" /></p>

                        <p class="contact-submit"> 
                            <html:submit styleId="contact-submit-login" value="Login" styleClass="submit" value="Login">Login</html:submit> 
                            <html:cancel styleId="contact-cancel-login" styleClass="submit" value="Cancel">Cancel</html:cancel>
                            </p>
                    </html:form>
                </div>

            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
