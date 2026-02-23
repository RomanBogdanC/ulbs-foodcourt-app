<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Plată - Cantina ULBS</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header">
                <div class="header-content">
                    <h1>💳 Plată</h1>
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">← Înapoi la coș</a>
                </div>
            </header>

            <main class="container">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div class="payment-card">
                    <h2>Confirmare comandă</h2>
                    <p class="payment-info">Verifică sumarul și apasă butonul pentru a finaliza comanda.</p>

                    <div class="payment-total">
                        <div class="total-line">
                            <span>Subtotal:</span>
                            <span>${subtotal} RON</span>
                        </div>
                        <c:if test="${not empty discountAmount && discountAmount.doubleValue() > 0}">
                            <div class="total-line discount-line">
                                <span>Discount
                                    <c:if test="${not empty person}">
                                        (${person.type})
                                    </c:if>:
                                </span>
                                <span>-${discountAmount} RON</span>
                            </div>
                        </c:if>
                        <div class="total-line total-final">
                            <span>Total de plătit:</span>
                            <span>${total} RON</span>
                        </div>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/order">
                        <button type="submit" class="btn btn-primary btn-lg btn-block">✓ Finalizează comanda</button>
                    </form>
                </div>
            </main>
        </body>

        </html>