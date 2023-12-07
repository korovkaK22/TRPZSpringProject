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


<c:if test="${user == null}">
    <div class="centerSign">
        Незареєстрований користувач
        <div><a href="/">Увійти</a></div>
    </div>
    </div>
</c:if>

<c:if test="${user != null}">
    <div class="mainDiv">
        <div class="centerSign">
            Створити користувача
        </div>

            <%--Імя пароль--%>
        <div class="authorization-container">
            <form id="userForm" action="/users/create-user-default" method="post">
                <input class="input-field" type="text" name="username" value="" placeholder=" Логін" required>
                <input class="input-field" type="text" name="password" value="" placeholder=" Пароль" required>
                <label for="myDropdown">Роль: </label>


                    <%--Ролі--%>
                    <!-- Радіокнопки -->
                    <div class="radio-buttons">
                        <label for="defaultRoleRadio">Вибрати дефолтну роль:</label>
                        <input type="radio" id="defaultRoleRadio" checked name="roleOption" value="default"
                               onclick="toggleVisibility()">

                        <label for="customRoleRadio">Ввести свою роль:</label>
                        <input type="radio" id="customRoleRadio" name="roleOption" value="custom"
                               onclick="toggleVisibility()">
                    </div>
                    <!-- Випадаючий список для дефолтної ролі -->
                    <div id="defaultRoleSelect" >
                        <select class="role-select" name="myDropdown" id="myDropdown">
                            <c:choose>
                                <c:when test="${users.size() == 0}">
                                    Ще немає станів
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${allDefaultStates}" var="state">
                                        <option value="${state.name}"><c:out value="${state.name}"/></option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                    <!-- Інпути для вводу своєї ролі -->
                    <div  id="customRoleInputs" style="display:none;">
                        <input class="input-field-small" type="text" disabled name="stateUsername" placeholder=" Ім'я" required>
                        <input class="input-field-small" type="text" disabled name="stateDirectory" placeholder=" Шлях" required>
                         Може редагувати  <input type="checkbox" disabled id="canWrite" name="stateCanWrite" value="true">
                         Адмін  <input type="checkbox" id="isAdmin" disabled name="stateIsAdmin" value="true">
                         Аккаунт активний?  <input type="checkbox" disabled id="isEnabled" checked name="stateIsEnabled" value="true">
                        <input class="input-field-small" type="number" min="0" disabled name="customDownloadSpeed" placeholder=" Швидкість на скачування" required>
                        <input class="input-field-small" type="number" min="0" disabled name="customUploadSpeed" placeholder=" Швидкість на вивантажування" required>

                    </div>

                    <button type="submit">Створити</button>
                </form>

                <c:if test="${failMessage != null}">
                <div class="failMessage"><c:out value="${failMessage} "/></div>
                </c:if>
        </div>
    </div>

</c:if>
</body>
</html>



<script>
    function toggleVisibility() {
        var defaultRoleSelect = document.getElementById('defaultRoleSelect');
        var customRoleInputs = document.getElementById('customRoleInputs');
        var isDefaultSelected = document.getElementById('defaultRoleRadio').checked;
        var form = document.getElementById('userForm');

        defaultRoleSelect.style.display = isDefaultSelected ? 'block' : 'none';
        customRoleInputs.style.display = isDefaultSelected ? 'none' : 'block';

        var defaultSelect = document.getElementById('myDropdown');
        var customInputs = customRoleInputs.querySelectorAll('input, select');
        if (isDefaultSelected) {
            form.action = "/users/create-user-default";
            defaultSelect.disabled = false;
            customInputs.forEach(input => input.disabled = true);
        } else {
            form.action = "/users/create-user-custom";
            defaultSelect.disabled = true;
            customInputs.forEach(input => input.disabled = false);
        }
    }
</script>
