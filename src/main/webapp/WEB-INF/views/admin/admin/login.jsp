<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="ro">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Login - Cantina ULBS</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <main class="container login-container">
            <div class="login-card">
                <h1>🔐 Autentificare Admin</h1>
                <p>Cantina ULBS - Panou de administrare</p>

                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                        <form method="post" action="${pageContext.request.contextPath}/admin/login">
                            <div class="form-group">
                                <label for="username">Utilizator</label>
                                <input type="text" id="username" name="username" class="input" required autofocus>
                            </div>
                            <div class="form-group">
                                <label for="password">Parolă</label>
                                <input type="password" id="password" name="password" class="input" required>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block">Autentificare</button>
                        </form>

                        <p class="login-footer"><a href="${pageContext.request.contextPath}/menu">← Înapoi la meniu</a>
                        </p>
            </div>
        </main>
    </body>

    </html>