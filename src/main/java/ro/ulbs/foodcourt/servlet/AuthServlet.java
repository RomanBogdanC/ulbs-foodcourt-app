// Se ocupă de Login și Logout

package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ro.ulbs.foodcourt.ejb.AuthBean;
import ro.ulbs.foodcourt.entity.User;

import java.io.IOException;

// Această adnotare leagă clasa de link-ul "/auth" din browser
@WebServlet(name = "AuthServlet", value = "/auth")
public class AuthServlet extends HttpServlet {

    @EJB
    private AuthBean authBean;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        User loggedInUser = authBean.authenticate(user, pass);

        if (loggedInUser != null) {
            request.getSession().setAttribute("user", loggedInUser);
            // Redirecționăm în funcție de rol
            if (loggedInUser.getRole() == User.Role.MANAGER) {
                response.sendRedirect(request.getContextPath() + "/manager");
            } else {
                response.sendRedirect(request.getContextPath() + "/kitchen");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }
}