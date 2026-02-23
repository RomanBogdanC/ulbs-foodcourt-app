package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.ulbs.foodcourt.ejb.MenuBean;
import ro.ulbs.foodcourt.entity.Category;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MenuServlet", urlPatterns = { "/menu", "/menu/detail" })
public class MenuServlet extends HttpServlet {

    @EJB
    private MenuBean menuBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/menu/detail".equals(path)) {
            // Show product detail page
            String idParam = req.getParameter("id");
            if (idParam != null) {
                var item = menuBean.findMenuItem(Long.parseLong(idParam));
                req.setAttribute("item", item);
            }
            req.getRequestDispatcher("/WEB-INF/views/detail.jsp").forward(req, resp);
            return;
        }

        // Default: show menu grouped by categories
        List<Category> categories = menuBean.getAllCategories();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
