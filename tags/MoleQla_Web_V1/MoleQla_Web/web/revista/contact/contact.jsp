<%-- 
    Document   : cotact
    Created on : 26-abr-2014, 10:52:36
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
            <%@include file="headerContact.jsp" %>
        </header>
        <div id="contact" class="page">

            <!-- Contact Form -->
            <div class="row">
                <div class="span9">

                    <form id="contact-form" class="contact-form" action="#">
                        <p class="contact-name">
                            <input id="contact_name" type="text" placeholder="Full Name" value="" name="name" />
                        </p>
                        <p class="contact-email">
                            <input id="contact_email" type="text" placeholder="Email Address" value="" name="email" />
                        </p>
                        <p class="contact-message">
                            <textarea id="contact_message" placeholder="Your Message" name="message" rows="15" cols="40"></textarea>
                        </p>
                        <p class="contact-submit">
                            <a id="contact-submit" class="submit" href="#">Send Your Email</a>
                        </p>

                        <div id="response">

                        </div>
                    </form>

                </div>

                <div class="span3">
                    <div class="contact-details">
                        <h3>Contact Details</h3>
                        <ul>
                            <li><a href="#">hello@brushed.com</a></li>
                            <li>(916) 375-2525</li>
                            <li>
                                Brushed Studio
                                <br>
                                5240 Vanish Island. 105
                                <br>
                                Unknow
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- End Contact Form -->
        </div>
    </body>
</html>
