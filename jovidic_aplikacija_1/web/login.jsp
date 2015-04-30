<%-- 
    Document   : login
    Created on : Jun 12, 2014, 7:12:01 PM
    Author     : jovidic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            body{
                background-color: #fff;
            }
            
            .centered {
                position: absolute;
                margin: auto;
                top: 0;
                right: 0;
                bottom: 0;
                left: 0;
                width: 10em;
                height: 8em;

            }
            .input{
                margin: 0;
                width: 10em;
                height: 2em;
                padding: 0;
            }
            .btn{
                height: 3em;
                width: 10em;
                padding: 0;
                margin: 0;
            }
        </style>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="centered">
            <form action="j_security_check" method="POST">
                <input class="input" type="text" name="j_username">
                <br/>
                <input class="input" type="password" name="j_password">
                <br/>
                <input class="btn" type="submit" value="Login">
            </form>
        </div>
    </body>
</html>




