<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="content" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="stmt" uri="http://java.sun.com/jsp/jstl/core" %>



<html>
<body>
<content:base>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="index.jsp">Бренд</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="#">Ссылка</a>
                </li>
                <li class="nav-item dropdown active">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Каталог товаров
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="ProductDisplayServlet?category=outerwear">Одежда</a>
                        <a class="dropdown-item" href="ProductDisplayServlet?category=footwear">Обувь</a>
                    </div>
                </li>
            </ul>

<%--            Если пользователь не авторизован--%>
            <stmt:if test="${user == null}">
                <form class="form-inline my-2 my-lg-0">
                    <button class="btn btn-outline-success my-2 my-sm-0 mr-4" type="button">
                        <a href="cart.jsp">
                            <i class="fa fa-shopping-cart" style="font-size:24px"> </i>
                        </a>
                    </button>
                    <button class="btn btn-outline-success my-2 my-sm-0 mr-1" type="button">
                        <a href="form-registration.jsp">Регистрация</a>
                    </button>
                    <button class="btn btn-outline-success my-2 my-sm-0" type="button">
                        <a href="form-login.jsp">Вход</a>
                    </button>
                </form>
            </stmt:if>

<%--            Если пользователь авторизовался--%>
            <stmt:if test="${user != null}">
                <form class="form-inline my-2 my-lg-0">
                    <button class="btn btn-outline-success my-2 my-sm-0 mr-4" type="button">
                        <a href="cart.jsp">
                            <i class="fa fa-shopping-cart" style="font-size:24px"> </i>
                        </a>
                    </button>
                    <button class="btn btn-outline-success my-2 my-sm-0 mr-1" type="button">
                        <a href="#">${user.firstName}</a>
                    </button>
                    <button class="btn btn-outline-success my-2 my-sm-0" type="button">
                        <a href="LogoutServlet">Выход</a>
                    </button>
                </form>
            </stmt:if>

        </div>
    </nav>
</content:base>

</body>
</html>
