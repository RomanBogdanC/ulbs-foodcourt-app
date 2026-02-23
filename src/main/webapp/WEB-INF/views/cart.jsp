<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Coș - Cantina ULBS</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header">
                <div class="header-content">
                    <h1>🛒 Coșul tău</h1>
                    <a href="${pageContext.request.contextPath}/menu" class="btn btn-secondary">← Înapoi la meniu</a>
                </div>
            </header>

            <main class="container">
                <c:if test="${empty cartItems}">
                    <div class="empty-state">
                        <p>Coșul tău este gol.</p>
                        <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Vezi meniul</a>
                    </div>
                </c:if>

                <c:if test="${not empty cartItems}">
                    <div class="cart-wrapper">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Produs</th>
                                    <th>Preț unitar</th>
                                    <th>Cantitate</th>
                                    <th>Subtotal</th>
                                    <th>Acțiuni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ci" items="${cartItems}">
                                    <tr>
                                        <td>${ci.item.name}</td>
                                        <td>${ci.item.price} RON</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/cart"
                                                class="inline-form">
                                                <input type="hidden" name="action" value="update">
                                                <input type="hidden" name="itemId" value="${ci.item.id}">
                                                <input type="number" name="quantity" value="${ci.quantity}" min="0"
                                                    max="99" class="qty-input">
                                                <button type="submit"
                                                    class="btn btn-sm btn-secondary">Actualizează</button>
                                            </form>
                                        </td>
                                        <td>${ci.lineTotal} RON</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/cart">
                                                <input type="hidden" name="action" value="remove">
                                                <input type="hidden" name="itemId" value="${ci.item.id}">
                                                <button type="submit" class="btn btn-sm btn-danger">✕ Șterge</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Badge ID section -->
                        <div class="badge-section">
                            <h3>Legitimație (opțional)</h3>
                            <form method="post" action="${pageContext.request.contextPath}/cart" class="badge-form">
                                <input type="hidden" name="action" value="setBadge">
                                <input type="text" name="badgeId" value="${badgeId}"
                                    placeholder="Introdu Badge ID (ex: STU001)" class="input">
                                <button type="submit" class="btn btn-primary">Aplică</button>
                            </form>
                            <c:if test="${not empty badgeError}">
                                <p class="error-msg">${badgeError}</p>
                            </c:if>
                            <c:if test="${not empty person}">
                                <p class="success-msg">
                                    ✓ Identificat: <strong>${person.type}</strong>
                                    — Discount aplicat:
                                    <c:if test="${person.type == 'STUDENT'}">10%</c:if>
                                    <c:if test="${person.type == 'PROFESSOR'}">15%</c:if>
                                </p>
                            </c:if>
                        </div>

                        <!-- Totals -->
                        <div class="totals-section">
                            <div class="total-line">
                                <span>Subtotal:</span>
                                <span>${subtotal} RON</span>
                            </div>
                            <c:if test="${not empty discountAmount && discountAmount.doubleValue() > 0}">
                                <div class="total-line discount-line">
                                    <span>Discount:</span>
                                    <span>-${discountAmount} RON</span>
                                </div>
                            </c:if>
                            <div class="total-line total-final">
                                <span>Total:</span>
                                <span>${total} RON</span>
                            </div>
                        </div>

                        <div class="cart-actions">
                            <a href="${pageContext.request.contextPath}/order" class="btn btn-primary btn-lg">Continuă
                                spre plată →</a>
                            <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-danger">Golește coșul</button>
                            </form>
                        </div>
                    </div>
                </c:if>
            </main>
        </body>

        </html>