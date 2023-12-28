<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../../../css/default.css">
    <link rel="stylesheet" type="text/css" href="../../../css/userlist.css">
    <link rel="stylesheet" type="text/css" href="../../../css/authorization.css">
    <link rel="stylesheet" type="text/css" href="../../../css/homePageButtons.css">

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



<div class="user-list">

    <c:choose>
        <c:when test="${roles.size()==0}">Ще немає ролей</c:when>
        <c:otherwise>
            <div class="centerSign">
                Всі ролі:
            </div>
            <c:forEach items="${roles}" var="u">
                <div class="user">
                    <a href="/roles/${u.id}"><c:out value="${u.name}"/></a>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>


<%--Кнопочки--%>
<div class="buttons">
    <form action="/roles/create" method="get">
        <button class="green" type="submit">Створити нову роль</button>
    </form>
</div>


</body>
</html>