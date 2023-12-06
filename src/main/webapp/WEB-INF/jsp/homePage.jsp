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
    <div><a href="/createUser">Я гвинтик</a></div>
</div>

<div class="user-list">

    <c:choose>
        <c:when test="${users.size()==0}">Ще немає користувачів</c:when>
        <c:otherwise>
            <div class="centerSign">
                Користувачі:
            </div>
            <c:forEach items="${users}" var="u">
                <div class="user">
                    (<c:out value="${u.state.name}"/>) <a href="/users/${u.name}"><c:out value="${u.name}"/></a>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>


    <%--Кнопочки--%>
<div class="buttons">
    <form action="/home" method="get">
        <input type="hidden" name="page" value="${pageNumber - 1}" />
        <button type="submit" <c:if test="${pageNumber == 1}">disabled="disabled"</c:if>>Попередня сторінка</button>
    </form>
    <form action="/createUser" method="get">
        <button id="open-queue-button" type="submit">Створити користувача</button>
    </form>
    <form action="/home" method="get">
        <input type="hidden" name="page" value="${pageNumber + 1}" />
        <button type="submit" <c:if test="${!hasNextPage}">disabled="disabled"</c:if>>Наступна сторінка</button>
    </form>
</c:if>


</body>
</html>