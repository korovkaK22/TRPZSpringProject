<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../../css/default.css">
    <link rel="stylesheet" type="text/css" href="../../css/userlist.css">
    <link rel="stylesheet" type="text/css" href="../../css/authorization.css">
    <link rel="stylesheet" type="text/css" href="../../css/homePageButtons.css">

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

<c:if test="${user == null}">
    <div class="centerSign">
        Незареєстрований користувач
        <div><a href="/">Увійти</a></div>
    </div>
    </div>
</c:if>

<c:if test="${user != null}">


    <div class="centerSign">
        <div>Користувач <c:out value="${serverUser.name}"/></div>
    </div>

    <div class="statistic-container">
        <c:if test="${!serverUser.enabled}">
            <span style="color: red;">Don't Enabled</span><br><br>
        </c:if>

        Username: <c:out value="${serverUser.name}"/> <br>
        State: <c:out value="${serverUser.stateName}"/> <br>
        Home directory: <c:out value="${serverUser.homeDirectory}"/> <br>
        <c:if test="${serverUser.state.isAdmin}">
            <span style="color: red;">Administrator</span><br>
        </c:if>
        <c:if test="${serverUser.state.canWrite}">
            <span style="color: #27de27;">Can Write</span><br>
        </c:if>
        <c:if test="${!serverUser.state.canWrite}">
            <span style="color: red;">Can't Write</span><br>
        </c:if>
        Upload speed:${serverUser.state.uploadSpeed}(Bytes/s)<br>
        Download speed: ${serverUser.state.downloadSpeed} (Bytes/s)<br>
    </div>




</c:if>


</body>
</html>