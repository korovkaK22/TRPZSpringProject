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


<div class="centerSign">
    Статистика: Онлайн: (${info.currentUsersNumber}/${info.maxUsersNumber})
    <c:choose>
        <c:when test="${info.allOnlineUsersName.size()==0}"></c:when>
        <c:otherwise>
            <c:forEach items="${info.allOnlineUsersName}" var="n">
                <a href="/users/${n}"><c:out value="${n}"/></a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<div class="user-list">

    <c:choose>
        <c:when test="${users.size()==0}">Ще немає користувачів</c:when>
        <c:otherwise>
            <div class="centerSign">
                Всі користувачі:
            </div>
            <c:forEach items="${users}" var="u">
                <div class="user">
                    (<c:out value="${u.role.name}"/>) <a href="/users/${u.id}"><c:out value="${u.name}"/></a>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>


<%--Кнопочки--%>
<div class="buttons">
    <form action="/home" method="get">
        <input type="hidden" name="page" value="${pageNumber - 1}"/>
        <button type="submit" style="<c:if test='${pageNumber == 1}'>display: none;</c:if>">Попередня сторінка</button>
    </form>
    <form action="/createUser" method="get">
        <button class="green" type="submit">Створити користувача</button>
    </form>
    <form action="/roles" method="get">
        <button  type="submit">Ролі</button>
    </form>
    <form action="/home" method="get">
        <input type="hidden" name="page" value="${pageNumber + 1}"/>
        <button type="submit" style="<c:if test='${!hasNextPage}'>display: none;</c:if>">Наступна сторінка</button>
    </form>
</div>

<div class="globalSpeed">
    Ліміт швидкості для всіх користувачів
    <form action="/global-speed" method="post">
        <div class="globalSpeedInputs">
            <input class="input-field" type="text" name="downloadSpeed" value="${globalDownloadSpeed}"
                   placeholder=" Завантаження" required>
            <input class="input-field" type="text" name="uploadSpeed" value="${globalUploadSpeed}"
                   placeholder=" Вивантаження" required>
        </div>
        <button type="submit">Оновити швидкість</button>
    </form>
</div>


</body>
</html>