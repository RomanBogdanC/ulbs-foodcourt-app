<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ro">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Gestionare Meniu - Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header class="header header-admin">
                <div class="header-content">
                    <h1>📋 Gestionare Meniu</h1>
                    <div>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">←
                            Dashboard</a>
                        <a href="${pageContext.request.contextPath}/admin/logout"
                            class="btn btn-danger btn-sm">Logout</a>
                    </div>
                </div>
            </header>

            <main class="container">
                <!-- Add Category -->
                <section class="admin-section">
                    <h2>Categorii</h2>
                    <form method="post" action="${pageContext.request.contextPath}/admin/menu" class="inline-form">
                        <input type="hidden" name="action" value="addCategory">
                        <input type="text" name="categoryName" placeholder="Nume categorie nouă" class="input" required>
                        <button type="submit" class="btn btn-primary">+ Adaugă categorie</button>
                    </form>

                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nume</th>
                                <th>Acțiuni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cat" items="${categories}">
                                <tr>
                                    <td>${cat.id}</td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/menu"
                                            class="inline-form">
                                            <input type="hidden" name="action" value="editCategory">
                                            <input type="hidden" name="categoryId" value="${cat.id}">
                                            <input type="text" name="categoryName" value="${cat.name}"
                                                class="input input-sm">
                                            <button type="submit" class="btn btn-sm btn-secondary">Salvează</button>
                                        </form>
                                    </td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/menu"
                                            class="inline-form"
                                            onsubmit="return confirm('Sigur vrei să ștergi această categorie și toate produsele asociate?')">
                                            <input type="hidden" name="action" value="deleteCategory">
                                            <input type="hidden" name="categoryId" value="${cat.id}">
                                            <button type="submit" class="btn btn-sm btn-danger">✕ Șterge</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </section>

                <!-- Add Menu Item -->
                <section class="admin-section">
                    <h2>Produse</h2>
                    <form method="post" action="${pageContext.request.contextPath}/admin/menu" class="add-item-form">
                        <input type="hidden" name="action" value="addItem">
                        <div class="form-row">
                            <div class="form-group">
                                <label>Nume</label>
                                <input type="text" name="itemName" class="input" required>
                            </div>
                            <div class="form-group">
                                <label>Preț (RON)</label>
                                <input type="number" name="itemPrice" step="0.01" min="0" class="input" required>
                            </div>
                            <div class="form-group">
                                <label>Stoc</label>
                                <input type="number" name="itemStock" min="0" class="input" required>
                            </div>
                            <div class="form-group">
                                <label>Categorie</label>
                                <select name="itemCategoryId" class="input" required>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.id}">${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group flex-2">
                                <label>Descriere</label>
                                <input type="text" name="itemDescription" class="input">
                            </div>
                            <div class="form-group">
                                <label>URL Imagine</label>
                                <input type="text" name="itemImageUrl" class="input">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">+ Adaugă produs</button>
                    </form>

                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nume</th>
                                <th>Preț</th>
                                <th>Stoc</th>
                                <th>Categorie</th>
                                <th>Descriere</th>
                                <th>Imagine</th>
                                <th>Acțiuni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${menuItems}">
                                <tr>
                                    <td>${item.id}</td>
                                    <td colspan="7">
                                        <form method="post" action="${pageContext.request.contextPath}/admin/menu"
                                            class="edit-item-form">
                                            <input type="hidden" name="action" value="editItem">
                                            <input type="hidden" name="itemId" value="${item.id}">
                                            <div class="edit-row">
                                                <input type="text" name="itemName" value="${item.name}"
                                                    class="input input-sm">
                                                <input type="number" name="itemPrice" value="${item.price}" step="0.01"
                                                    class="input input-sm" style="width:80px">
                                                <input type="number" name="itemStock" value="${item.stock}" min="0"
                                                    class="input input-sm" style="width:60px">
                                                <select name="itemCategoryId" class="input input-sm">
                                                    <c:forEach var="cat" items="${categories}">
                                                        <option value="${cat.id}" ${cat.id==item.category.id
                                                            ? 'selected' : '' }>${cat.name}</option>
                                                    </c:forEach>
                                                </select>
                                                <input type="text" name="itemDescription" value="${item.description}"
                                                    class="input input-sm">
                                                <input type="text" name="itemImageUrl" value="${item.imageUrl}"
                                                    class="input input-sm" placeholder="URL imagine">
                                                <button type="submit" class="btn btn-sm btn-secondary">Salvează</button>
                                            </div>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/menu"
                                            class="inline-form"
                                            onsubmit="return confirm('Sigur vrei să ștergi acest produs?')">
                                            <input type="hidden" name="action" value="deleteItem">
                                            <input type="hidden" name="itemId" value="${item.id}">
                                            <button type="submit" class="btn btn-sm btn-danger">✕ Șterge</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </section>
            </main>
        </body>

        </html>