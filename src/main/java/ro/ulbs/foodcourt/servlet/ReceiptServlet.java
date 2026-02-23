package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.ulbs.foodcourt.ejb.OrderBean;
import ro.ulbs.foodcourt.entity.Order;

import java.io.IOException;

@WebServlet(name = "ReceiptServlet", urlPatterns = "/receipt")
public class ReceiptServlet extends HttpServlet {

    @EJB
    private OrderBean orderBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        Order order = orderBean.findOrder(Long.parseLong(orderIdParam));
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/receipt.jsp").forward(req, resp);
    }
}
