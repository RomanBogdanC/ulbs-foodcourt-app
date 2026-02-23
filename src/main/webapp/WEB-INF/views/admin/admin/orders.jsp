<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Comenzi - Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header header-admin">
                <div class="header-content">
                    <h1>📦 Comenzi</h1>
                    <div>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">←
                            Dashboard</a>
                        <a href="${pageContext.request.contextPath}/admin/logout"
                            class="btn btn-danger btn-sm">Logout</a>
                    </div>
                </div>
            </header>

            <main class="container">
                <!-- Filters -->
                <section class="admin-section">
                    <h2>Filtre</h2>
                    <form method="get" action="${pageContext.request.contextPath}/admin/orders" class="filter-form">
                        <div class="form-group">
                            <label>Filtrează după dată:</label>
                            <input type="date" name="filterDate" value="${filterDate}" class="input">
                        </div>
                        <div class="form-group">
                            <label>Sortare:</label>
                            <select name="sort" class="input">
                                <option value="asc" ${ascending ? 'selected' : '' }>Dată ↑ (crescător)</option>
                                <option value="desc" ${!ascending ? 'selected' : '' }>Dată ↓ (descrescător)</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Aplică</button>
                    </form>
                </section>

                <!-- Daily total -->
                <c:if test="${not empty dailyTotal}">
                    <div class="stats-card">
                        <h3>Total vânzări pe ${filterDate}:</h3>
                        <p class="stats-value">${dailyTotal} RON</p>
                    </div>
                </c:if>

                <!-- Orders table -->
                <section class="admin-section">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Data/Ora</th>
                                <th>Produse</th>
                                <th>Discount</th>
                                <th>Total</th>
                                <th>Badge ID</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
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
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.person != null}">${order.person.badgeId}</c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge badge-success">${order.status}</span></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty orders}">
                                <tr>
                                    <td colspan="7" class="text-center">Nu există comenzi.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </section>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage - 1}&sort=${ascending ? 'asc' : 'desc'}&filterDate=${filterDate}"
                                class="btn btn-sm btn-secondary">← Anterior</a>
                        </c:if>
                        <span class="page-info">Pagina ${currentPage} din ${totalPages}</span>
                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage + 1}&sort=${ascending ? 'asc' : 'desc'}&filterDate=${filterDate}"
                                class="btn btn-sm btn-secondary">Următor →</a>
                        </c:if>
                    </div>
                </c:if>
            </main>
        </body>

        </html>