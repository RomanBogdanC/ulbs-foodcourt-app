<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard Admin - Cantina ULBS</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header header-admin">
                <div class="header-content">
                    <h1>⚙️ Panou Admin</h1>
                    <div>
                        <span class="user-badge">👤 ${sessionScope.loggedUser.username}
                            (${sessionScope.loggedUser.role})</span>
                        <a href="${pageContext.request.contextPath}/admin/logout"
                            class="btn btn-danger btn-sm">Logout</a>
                    </div>
                </div>
            </header>

            <main class="container">
                <div class="dashboard-grid">
                    <a href="${pageContext.request.contextPath}/admin/menu" class="dashboard-card">
                        <span class="dashboard-icon">📋</span>
                        <h3>Gestionare Meniu</h3>
                        <p>Adaugă, editează sau șterge produse și categorii</p>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="dashboard-card">
                        <span class="dashboard-icon">📦</span>
                        <h3>Comenzi</h3>
                        <p>Vizualizează toate comenzile și totalul vânzărilor</p>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/statistics" class="dashboard-card">
                        <span class="dashboard-icon">📊</span>
                        <h3>Statistici</h3>
                        <p>Statistici per produs și per legitimație</p>
                    </a>
                    <a href="${pageContext.request.contextPath}/menu" class="dashboard-card">
                        <span class="dashboard-icon">🍽️</span>
                        <h3>Vezi Meniul Public</h3>
                        <p>Vizualizează meniul așa cum îl văd clienții</p>
                    </a>
                </div>
            </main>
        </body>

        </html>