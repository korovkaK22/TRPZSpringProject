<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../../css/default.css">
    <link rel="stylesheet" type="text/css" href="../../css/userlist.css">
    <link rel="stylesheet" type="text/css" href="../../css/user.css">
    <link rel="stylesheet" type="text/css" href="../../css/homePageButtons.css">
    <link rel="stylesheet" type="text/css" href="../../css/authorization.css">

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
    <div>Користувач <c:out value="${serverUser.name}"/></div>
</div>

<div class="statistic-container">
    <c:if test="${!serverUser.enabled}">
    <span style="color: red;">Don't Enabled</span><br><br>
    </c:if>

    Username:
    <c:out value="${serverUser.name}"/> <br>
    Role:
    <c:out value="${serverUser.roleName}"/> <br>
    Home directory:
    <c:out value="${serverUser.homeDirectory}"/> <br>
    <c:if test="${serverUser.role.isAdmin}">
    <span style="color: red;">Administrator</span><br>
    </c:if>
    <c:if test="${serverUser.role.canWrite}">
    <span style="color: #27de27;">Can Write</span><br>
    </c:if>
    <c:if test="${!serverUser.role.canWrite}">
    <span style="color: red;">Can't Write</span><br>
    </c:if>
    Upload speed:${serverUser.role.uploadSpeed}(Bytes/s)<br>
    Download speed: ${serverUser.role.downloadSpeed} (Bytes/s)<br>


    <div id="defaultRoleSelect">
        <form id="userForm" action="/users/change-role" method="post">
            <input type="hidden" name="id" value="${serverUser.id}"/>
            <select class="role-select" name="myDropdown" id="myDropdown">
                <c:choose>
                    <c:when test="${roles.size() == 0}">
                        Ще немає ролей
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${roles}" var="role">
                            <option value="${role.id}"  <c:if
                                    test="${role.name == serverUser.roleName}"> selected </c:if>><c:out
                                    value="${role.name}"/></option>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </select>
            <button class="small-button green" type="submit">Змінити роль</button>
        </form>
    </div>


    <form class="snapshots" action="/users/delete-user" method="post">
        <input type="hidden" name="username" value="${serverUser.name}"/>
        <button class="small-button red" type="submit">Видалити</button>
    </form>


</body>
</html>