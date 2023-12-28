<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page errorPage="errorPage.jsp" %>--%>

<html>
<head>
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="../../css/default.css">
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




    <div class="mainDiv">
        <div class="centerSign">
            Створити користувача
        </div>

            <%--Імя пароль--%>
        <div class="authorization-container">
            <form id="userForm" action="/users/create-user" method="post">
                <input class="input-field" type="text" name="username" value="" placeholder=" Логін" required>
                <input class="input-field" type="text" name="password" value="" placeholder=" Пароль" required>
                <select class="role-select" name="myDropdown" id="myDropdown">
                    <c:choose>
                        <c:when test="${roles.size() == 0}">
                            Ще немає ролей
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.id}"><c:out
                                        value="${role.name}"/></option>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </select>
                <button type="submit">Створити</button>
            </form>

            <c:if test="${failMessage != null}">
                <div class="failMessage"><c:out value="${failMessage} "/></div>
            </c:if>
        </div>
    </div>


</body>
</html>


