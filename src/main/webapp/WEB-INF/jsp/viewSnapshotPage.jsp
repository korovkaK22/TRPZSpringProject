<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../../css/default.css">
    <link rel="stylesheet" type="text/css" href="../../css/userlist.css">

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
    <div>Знімок "<c:out value="${memento.name}"/>"</div>
</div>

<div class="statistic-container">
    User: <a href="/users/${memento.username}"><c:out value="${memento.username}"/></a><br>
    State: <c:out value="${memento.stateName}"/> <br>

    Home directory: <c:out value="${state.homeDir}"/> <br>
    <c:if test="${state.isAdmin}">
        <span style="color: red;">Administrator</span><br>
    </c:if>
    <c:if test="${state.canWrite}">
        <span style="color: #27de27;">Can Write</span><br>
    </c:if>
    <c:if test="${!state.canWrite}">
        <span style="color: red;">Can't Write</span><br>
    </c:if>
    Upload speed:${state.uploadSpeed}(Bytes/s)<br>
    Download speed: ${state.downloadSpeed} (Bytes/s)<br>
</div>

<div class="buttons">
    <form action="/snapshots/delete" method="post">
        <input type="hidden" name="id" value="${memento.id}"/>
        <button class="red" type="submit">Видалити знімок</button>
    </form>
    <form action="/snapshots/set-to-user" method="post">
        <input type="hidden" name="id" value="${memento.id}"/>
        <button class="green" type="submit">Застосувати знімок</button>
    </form>
    </c:if>


</body>
</html>