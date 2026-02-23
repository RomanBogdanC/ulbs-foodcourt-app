// Ecranul pentru bucătari

package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ro.ulbs.foodcourt.ejb.OrderBean;
import ro.ulbs.foodcourt.entity.User;

import java.io.IOException;

@WebServlet(name = "KitchenServlet", value = "/kitchen")
public class KitchenServlet extends HttpServlet {

    @EJB
    private OrderBean orderBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificăm dacă utilizatorul e logat și e bucătar
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != User.Role.KITCHEN) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Trimitem lista de comenzi către pagina JSP
        request.setAttribute("activeOrders", orderBean.getActiveOrders());
        request.getRequestDispatcher("/kitchen.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");

        if (orderIdStr != null && status != null) {
            orderBean.updateOrderStatus(Long.parseLong(orderIdStr), status);
        }

        // Reîncărcăm pagina după modificare
        response.sendRedirect(request.getContextPath() + "/kitchen");
    }
}