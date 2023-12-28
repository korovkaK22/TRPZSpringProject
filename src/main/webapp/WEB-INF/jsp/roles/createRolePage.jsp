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
        Створити роль
    </div>
        <div class="authorization-container">
            <form id="userForm" action="/roles/create" method="post">
            <input class="input-field-small" type="text"  name="roleUsername" placeholder=" Ім'я" required>
            <input class="input-field-small" type="text"  name="roleDirectory" placeholder=" Шлях" required>
            Може редагувати  <input type="checkbox"  id="canWrite" name="roleCanWrite" value="true">
            Адмін  <input type="checkbox" id="isAdmin"  name="roleIsAdmin" value="true">
            Аккаунт активний?  <input type="checkbox"  id="isEnabled" checked name="roleIsEnabled" value="true">
            <input class="input-field-small" type="number" min="0"  name="customDownloadSpeed" placeholder=" Швидкість на скачування" required>
            <input class="input-field-small" type="number" min="0"  name="customUploadSpeed" placeholder=" Швидкість на вивантажування" required>
                <button type="submit">Створити</button>
            </form>

        <c:if test="${failMessage != null}">
            <div class="failMessage"><c:out value="${failMessage} "/></div>
        </c:if>
        </div>
    </div>



</body>
</html>


