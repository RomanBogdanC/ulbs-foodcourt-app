<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Detalii Produs - Cantina ULBS</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header">
                <div class="header-content">
                    <a href="${pageContext.request.contextPath}/menu" class="btn btn-secondary">← Înapoi la meniu</a>
                    <h1>🍽️ Cantina ULBS</h1>
                </div>
            </header>

            <main class="container">
                <c:if test="${not empty item}">
                    <div class="detail-card">
                        <c:if test="${not empty item.imageUrl}">
                            <img src="${item.imageUrl}" alt="${item.name}" class="detail-img">
                        </c:if>
                        <c:if test="${empty item.imageUrl}">
                            <div class="detail-img placeholder-img">🍽️</div>
                        </c:if>
                        <div class="detail-body">
                            <h2>${item.name}</h2>
                            <p class="detail-category">Categorie: <strong>${item.category.name}</strong></p>
                            <p class="detail-desc">${item.description}</p>
                            <p class="detail-price">${item.price} RON</p>
                            <p class="detail-stock">
                                Stoc:
                                <c:choose>
                                    <c:when test="${item.stock > 0}">
                                        <span class="badge badge-success">${item.stock} disponibile</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Indisponibil</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${item.stock > 0}">
                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="itemId" value="${item.id}">
                                    <button type="submit" class="btn btn-primary">+ Adaugă în coș</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </main>
        </body>

        </html>