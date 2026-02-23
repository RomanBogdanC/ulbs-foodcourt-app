<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Statistici - Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header header-admin">
                <div class="header-content">
                    <h1>📊 Statistici</h1>
                    <div>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">←
                            Dashboard</a>
                        <a href="${pageContext.request.contextPath}/admin/logout"
                            class="btn btn-danger btn-sm">Logout</a>
                    </div>
                </div>
            </header>

            <main class="container">
                <!-- Date range filter -->
                <section class="admin-section">
                    <h2>Selectează intervalul</h2>
                    <form method="get" action="${pageContext.request.contextPath}/admin/statistics" class="filter-form">
                        <div class="form-group">
                            <label>De la:</label>
                            <input type="date" name="fromDate" value="${fromDate}" class="input" required>
                        </div>
                        <div class="form-group">
                            <label>Până la:</label>
                            <input type="date" name="toDate" value="${toDate}" class="input" required>
                        </div>
                        <div class="form-group">
                            <label>Badge ID (opțional):</label>
                            <input type="text" name="badgeId" value="${badgeId}" placeholder="ex: STU001" class="input">
                        </div>
                        <button type="submit" class="btn btn-primary">Caută</button>
                    </form>
                </section>

                <!-- Per-product stats -->
                <c:if test="${not empty productStats}">
                    <section class="admin-section">
                        <h2>Statistici per produs (${fromDate} — ${toDate})</h2>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Produs</th>
                                    <th>Unități vândute</th>
                                    <th>Venituri totale</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row" items="${productStats}">
                                    <tr>
                                        <td>${row[0]}</td>
                                        <td>${row[1]}</td>
                                        <td>${row[2]} RON</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>

                <!-- Per badge ID stats -->
                <c:if test="${not empty badgeId && not empty fromDate}">
                    <section class="admin-section">
                        <h2>Comenzi pentru Badge ID: ${badgeId}</h2>

                        <c:if test="${not empty badgeTotal}">
                            <div class="stats-card">
                                <h3>Total cheltuit:</h3>
                                <p class="stats-value">${badgeTotal} RON</p>
                            </div>
                        </c:if>

                        <c:if test="${not empty badgeOrders}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>ID Comandă</th>
                                        <th>Data/Ora</th>
                                        <th>Produse</th>
                                        <th>Discount</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${badgeOrders}">
                                        <tr>
                                            <td>#${order.id}</td>
                                            <td>${order.dateTime}</td>
                                            <td>
                                                <c:forEach var="oi" items="${order.items}">
                                                    ${oi.menuItem.name} x${oi.quantity}<br>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when
                                                        test="${order.discountAmount != null && order.discountAmount.doubleValue() > 0}">
                                                        -${order.discountAmount} RON
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><strong>${order.totalPrice} RON</strong></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty badgeOrders}">
                            <p>Nu s-au găsit comenzi pentru acest Badge ID în intervalul selectat.</p>
                        </c:if>
                    </section>
                </c:if>
            </main>
        </body>

        </html>