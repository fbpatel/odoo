<%-- 
    Document   : cotact
    Created on : 26-abr-2014, 10:52:36
    Author     : Rafa
--%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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
        <script type="text/javascript">
            $(function() {

                $("#contact_name").attr("placeholder", "Full name *");
                $("#contact_email").attr("placeholder", "Email Address *");
                $("#contact_message").attr("placeholder", "Your Message *");

            });

        </script>
    </head>
    <body>
        <%@include file="../circulo.html" %>

        <header>
            <%@include file="headerContact.jsp" %>
        </header>
        <div id="contact" class="page">
            <div class="container">

                <div class="span12">
                    <div class="title-page">
                        <h2 class="title">Contact with MoleQla</h2>
                        <h3 class="title-description">Send us your question and the editorial team will resolve</h3>
                    </div>
                </div>

                <!-- Contact Form -->
                <div class="row">
                    <div class="span9">

                        <html:form action="/revista/contact/contact" styleId="contact-form" styleClass="contact-form">
                            <p><bean:write name="ContactActionForm" property="errorMsg" filter="false"/></p>
                            <p><bean:write name="ContactActionForm" property="msg" filter="false"/></p>
                            <p class="contact-name">
                                <html:text styleId="contact_name" property="name"/>
                            </p>
                            <p class="contact-email">
                                <html:text styleId="contact_email" property="email" />
                            </p>
                            <p class="contact-message">
                                <html:textarea styleId="contact_message" property="message" rows="15" cols="40"/>
                            </p>
                            <p class="contact-submit">
                                <html:submit styleClass="submit" value="Send Your Email">Send Your Email</html:submit>
                                <html:cancel styleClass="submit" value="Cancel">Cancel</html:cancel>
                                </p>

                                <div id="response">

                                </div>
                        </html:form>

                    </div>

                    <div class="span3">
                        <div class="contact-details">
                            <h3>Contact Details</h3>
                            <ul>
                                <!--<li><a href="#">moleqlanotify@gmail.com</a></li>-->
                                <li>954 34 92 00</li>
                                <li>
                                    Carretera Utrera, km. 1
                                    <br>
                                    41013 Sevilla
                                    <br>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
