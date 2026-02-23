<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Bon - Cantina ULBS</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <style>
                @media print {
                    .no-print {
                        display: none !important;
                    }

                    body {
                        background: white;
                    }

                    .receipt-card {
                        box-shadow: none;
                        border: 1px solid #ccc;
                    }
                }
            </style>
        </head>

        <body>
            <header class="header no-print">
                <div class="header-content">
                    <h1>🧾 Bon de comandă</h1>
                </div>
            </header>

            <main class="container">
                <div class="receipt-card">
                    <div class="receipt-header">
                        <h2>Cantina ULBS</h2>
                        <p>Bon fiscal #${order.id}</p>
                        <p>${order.dateTime}</p>
                    </div>

                    <table class="table receipt-table">
                        <thead>
                            <tr>
                                <th>Produs</th>
                                <th>Cant.</th>
                                <th>Preț unit.</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="oi" items="${order.items}">
                                <tr>
                                    <td>${oi.menuItem.name}</td>
                                    <td>${oi.quantity}</td>
                                    <td>${oi.unitPrice} RON</td>
                                    <td>${oi.subtotal} RON</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="receipt-totals">
                        <c:set var="subtotal"
                            value="${order.totalPrice.add(order.discountAmount != null ? order.discountAmount : 0)}" />
                        <c:if test="${order.discountAmount != null && order.discountAmount.doubleValue() > 0}">
                            <div class="total-line">
                                <span>Subtotal:</span>
                                <span>${order.totalPrice.add(order.discountAmount)} RON</span>
                            </div>
                            <div class="total-line discount-line">
                                <span>Discount
                                    <c:if test="${order.person != null}">
                                        (${order.person.type})
                                    </c:if>:
                                </span>
                                <span>-${order.discountAmount} RON</span>
                            </div>
                        </c:if>
                        <div class="total-line total-final">
                            <span>TOTAL:</span>
                            <span>${order.totalPrice} RON</span>
                        </div>
                    </div>

                    <c:if test="${order.person != null}">
                        <p class="receipt-badge">Badge ID: ${order.person.badgeId} (${order.person.type})</p>
                    </c:if>

                    <div class="receipt-footer">
                        <p>Vă mulțumim! Poftă bună!</p>
                    </div>
                </div>

                <div class="receipt-actions no-print">
                    <button onclick="window.print()" class="btn btn-primary btn-lg">🖨️ Printează bonul</button>
                    <a href="${pageContext.request.contextPath}/menu" class="btn btn-secondary btn-lg">Comandă nouă</a>
                </div>
            </main>
        </body>

        </html>