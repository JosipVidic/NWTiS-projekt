<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/displaytag.css"/>
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/osnovna.css"/>
        <title>meteo data</title>
    </head>
    <body>
            <ul>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/MeteoData">Svi meteo podaci</a></button></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/AdrData">Sve adrese</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/LogData">Dnevnik</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/SaveData">Spremi adresu</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/NowData">Važeći meteo</a></li>
            <li><a class="linkas" href="${pageContext.servletContext.contextPath}/GeoData">Geolokacijski podaci</a></li>
                        <li><a class="linkas" href="${pageContext.servletContext.contextPath}/AppData">Aplikacijski Dnevnik</a></li>
            </ul>
             <br/>

        <h1>Ispis dnevnika</h1>

        <display:table name="sessionScope.dnevnik" pagesize="25">
            <display:column property="komanda" title="Komanda" sortable="true"/>  
            <display:column property="korisnik" title="Korisnik" sortable="true"/>         
            <display:column property="vrijeme" title="Vrijeme" sortable="true"/>
        </display:table>
    </form>
</body>
</html>
