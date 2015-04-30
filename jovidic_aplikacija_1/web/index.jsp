<%-- 
    Document   : index
    Created on : May 29, 2014, 7:40:13 PM
    Author     : jovidic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/osnovna.css"/>
        <title>JSP Page</title>
    </head>
    <body>

        <form action="ObradaAkcija" method="POST">
            <ul>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/MeteoData">Svi meteo podaci</a></button></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/AdrData">Sve adrese</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/LogData">Dnevnik</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/SaveData">Spremi adresu</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/NowData">Važeći meteo</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/GeoData">Geolokacijski podaci</a></li>
                        <li><a class="linkas" href="${pageContext.servletContext.contextPath}/AppData">Aplikacijski Dnevnik</a></li>
            </ul>
        </form>

    </body>
</html>
