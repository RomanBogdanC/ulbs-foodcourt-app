<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cantina ULBS - Meniu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header class="header">
        <div class="header-content">
            <h1>🍽️ Cantina ULBS</h1>
            <p class="subtitle">Alege din meniul nostru</p>
            <a href="${pageContext.request.contextPath}/cart" class="btn btn-cart">
                🛒 Coș
            </a>
        </div>
    </header>

    <main class="container">
        <c:forEach var="category" items="${categories}">
            <section class="category-section">
                <h2 class="category-title">${category.name}</h2>
                <div class="menu-grid">
                    <c:forEach var="item" items="${category.items}">
                        <div class="menu-card ${item.stock <= 0 ? 'unavailable' : ''}">
                            <c:if test="${not empty item.imageUrl}">
                                <img src="${item.imageUrl}" alt="${item.name}" class="menu-card-img">
                            </c:if>
                            <c:if test="${empty item.imageUrl}">
                                <div class="menu-card-img placeholder-img">🍽️</div>
                            </c:if>
                            <div class="menu-card-body">
                                <h3 class="menu-card-title">
                                    <a href="${pageContext.request.contextPath}/menu/detail?id=${item.id}">${item.name}</a>
                                </h3>
                                <p class="menu-card-desc">${item.description}</p>
                                <div class="menu-card-footer">
                                    <span class="price">${item.price} RON</span>
                                    <c:choose>
                                        <c:when test="${item.stock <= 0}">
                                            <span class="badge badge-danger">Indisponibil</span>
                                        </c:when>
                                        <c:otherwise>
                                            <form method="post" action="${pageContext.request.contextPath}/cart">
                                                <input type="hidden" name="action" value="add">
                                                <input type="hidden" name="itemId" value="${item.id}">
                                                <button type="submit" class="btn btn-primary btn-sm">+ Adaugă</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </c:forEach>

        <c:if test="${empty categories}">
            <div class="empty-state">
                <p>Nu există produse în meniu momentan.</p>
            </div>
        </c:if>
    </main>

    <footer class="footer">
        <p>&copy; 2026 Cantina ULBS | <a href="${pageContext.request.contextPath}/admin/login">Admin</a></p>
    </footer>
</body>
</html>
