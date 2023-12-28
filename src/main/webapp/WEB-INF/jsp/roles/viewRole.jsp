<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../../../css/default.css">
    <link rel="stylesheet" type="text/css" href="../../../css/userlist.css">
    <link rel="stylesheet" type="text/css" href="../../../css/user.css">
    <link rel="stylesheet" type="text/css" href="../../../css/homePageButtons.css">
    <link rel="stylesheet" type="text/css" href="../../../css/authorization.css">

    <%--Гугл шрифт на черги--%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Alegreya:wght@500&display=swap" rel="stylesheet">

    <%--Гугл шрифт на верхній надпис--%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Alegreya:wght@500&family=Vollkorn:wght@500&display=swap"
          rel="stylesheet">

</head>
<body>


<div class="centerSign">
    <div>Роль <c:out value="${role.name}"/></div>
</div>

<div class="statistic-container">
    <c:if test="${!role.isEnabled}">
    <span style="color: red;">Don't Enabled</span><br><br>
    </c:if>

    Name:
    <c:out value="${role.name}"/> <br>
    Home directory:
    <c:out value="${role.homeDir}"/> <br>
    <c:if test="${role.isAdmin}">
    <span style="color: red;">Administrator</span><br>
    </c:if>
    <c:if test="${role.canWrite}">
    <span style="color: #27de27;">Can Write</span><br>
    </c:if>
    <c:if test="${!role.canWrite}">
    <span style="color: red;">Can't Write</span><br>
    </c:if>
    Upload speed:${role.uploadSpeed}(Bytes/s)<br>
    Download speed: ${role.downloadSpeed} (Bytes/s)<br>



    <form class="snapshots" action="/roles/${role.id}/edit" method="get">
        <button class="small-button green" type="submit">Редагувати</button>
    </form>

    <form class="snapshots" action="/roles/delete" method="post">
            <input type="hidden" name="username" value="${role.id}"/>
            <button class="small-button red" type="submit">Видалити</button>
    </form>


</body>
</html>