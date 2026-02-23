package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.ulbs.foodcourt.ejb.MenuBean;
import ro.ulbs.foodcourt.entity.Category;
import ro.ulbs.foodcourt.entity.MenuItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = { "/admin/dashboard", "/admin/menu" })
public class AdminServlet extends HttpServlet {

    @EJB
    private MenuBean menuBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/admin/dashboard".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
            return;
        }

        if ("/admin/menu".equals(path)) {
            List<Category> categories = menuBean.getAllCategories();
            List<MenuItem> items = menuBean.getAllMenuItems();
            req.setAttribute("categories", categories);
            req.setAttribute("menuItems", items);
            req.getRequestDispatcher("/WEB-INF/views/admin/menu.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action != null ? action : "") {
            case "addCategory": {
                String name = req.getParameter("categoryName");
                if (name != null && !name.trim().isEmpty()) {
                    menuBean.addCategory(new Category(name.trim()));
                }
                break;
            }
            case "editCategory": {
                Long id = Long.parseLong(req.getParameter("categoryId"));
                String name = req.getParameter("categoryName");
                Category c = menuBean.findCategory(id);
                if (c != null && name != null) {
                    c.setName(name.trim());
                    menuBean.updateCategory(c);
                }
                break;
            }
            case "deleteCategory": {
                Long id = Long.parseLong(req.getParameter("categoryId"));
                menuBean.deleteCategory(id);
                break;
            }
            case "addItem": {
                String name = req.getParameter("itemName");
                BigDecimal price = new BigDecimal(req.getParameter("itemPrice"));
                String desc = req.getParameter("itemDescription");
                String imageUrl = req.getParameter("itemImageUrl");
                int stock = Integer.parseInt(req.getParameter("itemStock"));
                Long catId = Long.parseLong(req.getParameter("itemCategoryId"));
                Category cat = menuBean.findCategory(catId);
                if (cat != null) {
                    MenuItem item = new MenuItem(name.trim(), price, desc, stock, cat);
                    item.setImageUrl(imageUrl);
                    menuBean.addMenuItem(item);
                }
                break;
            }
            case "editItem": {
                Long id = Long.parseLong(req.getParameter("itemId"));
                MenuItem item = menuBean.findMenuItem(id);
                if (item != null) {
                    item.setName(req.getParameter("itemName").trim());
                    item.setPrice(new BigDecimal(req.getParameter("itemPrice")));
                    item.setDescription(req.getParameter("itemDescription"));
                    item.setImageUrl(req.getParameter("itemImageUrl"));
                    item.setStock(Integer.parseInt(req.getParameter("itemStock")));
                    Long catId = Long.parseLong(req.getParameter("itemCategoryId"));
                    item.setCategory(menuBean.findCategory(catId));
                    menuBean.updateMenuItem(item);
                }
                break;
            }
            case "deleteItem": {
                Long id = Long.parseLong(req.getParameter("itemId"));
                menuBean.deleteMenuItem(id);
                break;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/menu");
    }
}
