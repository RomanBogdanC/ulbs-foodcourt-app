package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ro.ulbs.foodcourt.ejb.MenuBean;
import ro.ulbs.foodcourt.ejb.OrderBean;
import ro.ulbs.foodcourt.ejb.PersonBean;
import ro.ulbs.foodcourt.entity.MenuItem;
import ro.ulbs.foodcourt.entity.Order;
import ro.ulbs.foodcourt.entity.Person;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    @EJB
    private OrderBean orderBean;

    @EJB
    private MenuBean menuBean;

    @EJB
    private PersonBean personBean;

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        // Compute subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            MenuItem item = menuBean.findMenuItem(entry.getKey());
            if (item != null) {
                subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }
        }

        // Check for discount
        String badgeId = (String) session.getAttribute("badgeId");
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (badgeId != null && !badgeId.isEmpty()) {
            Person person = personBean.findByBadgeId(badgeId);
            if (person != null) {
                discountAmount = personBean.calculateDiscount(subtotal, person);
                req.setAttribute("person", person);
            }
        }

        BigDecimal total = subtotal.subtract(discountAmount);
        req.setAttribute("subtotal", subtotal);
        req.setAttribute("discountAmount", discountAmount);
        req.setAttribute("total", total);

        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        String badgeId = (String) session.getAttribute("badgeId");

        try {
            Order order = orderBean.createOrder(cart, badgeId);

            // Clear cart after successful order
            session.removeAttribute("cart");
            session.removeAttribute("badgeId");

            // Redirect to receipt
            resp.sendRedirect(req.getContextPath() + "/receipt?orderId=" + order.getId());
        } catch (IllegalStateException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
        }
    }
}
