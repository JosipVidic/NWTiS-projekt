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

        
        <h1>Ispis meteo podataka</h1>
        
        
        <form action="${pageContext.servletContext.contextPath}/MeteoData" method="POST">
            <label>Filtriraj po adresi: </label> 
             <br/>
            <input type="text" name="adresaFilter" value="${adresaFilter}"/>
             <br/>
            <label>    Filtriraj po datumu: </label> 
             <br/>
            <label>Od </label> <input type="text" name="datumOd" value="${datumOd}"/>
             <br/>
           <label>Do </label>  <input type="text" name="datumDo" value="${datumDo}"/>
            <br/>
            
            <select name="odabir" size="5" id="s">
                     <option value="5">5</option>
                     <option value="10">10</option>
                     <option value="20">25</option>
                     <option value="50">50</option>
                     <option value="100">100</option>
            </select>
            <input type="submit" value="Filtriraj" /><br/>
            
        <display:table name="sessionScope.list" pagesize="${odabir}">
            <display:column property="adresaMeteoPodatka" title="Adresa" sortable="true"/>           
            <display:column property="temperature" title="Temperatura" sortable="true"/>
            <display:column property="humidity" title="Vlaga" sortable="true"/>
            <display:column property="pressureSeaLevel" title="Tlak" sortable="true"/>
            <display:column property="windSpeed" title="Brzina vjetra" sortable="true"/>
            <display:column property="datumUnosa" title="Datum unosa" sortable="true"/>
        </display:table>
    </form>
</body>
</html>
