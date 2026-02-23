package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.ulbs.foodcourt.ejb.AuthBean;
import ro.ulbs.foodcourt.entity.User;

import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = { "/admin/login", "/admin/logout" })
public class AuthServlet extends HttpServlet {

    @EJB
    private AuthBean authBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/admin/logout".equals(path)) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }

        // Show login form
        req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = authBean.authenticate(username, password);

        if (user != null) {
            req.getSession().setAttribute("loggedUser", user);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }

        req.setAttribute("error", "Invalid username or password");
        req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
    }
}
