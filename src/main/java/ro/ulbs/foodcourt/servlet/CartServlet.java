package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ro.ulbs.foodcourt.ejb.MenuBean;
import ro.ulbs.foodcourt.ejb.PersonBean;
import ro.ulbs.foodcourt.entity.MenuItem;
import ro.ulbs.foodcourt.entity.Person;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "CartServlet", urlPatterns = "/cart")
public class CartServlet extends HttpServlet {

    @EJB
    private MenuBean menuBean;

    @EJB
    private PersonBean personBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Long, Integer> cart = getCart(session);

        // Build cart display data
        List<Map<String, Object>> cartItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            MenuItem item = menuBean.findMenuItem(entry.getKey());
            if (item != null) {
                Map<String, Object> ci = new HashMap<>();
                ci.put("item", item);
                ci.put("quantity", entry.getValue());
                BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                ci.put("lineTotal", lineTotal);
                cartItems.add(ci);
                subtotal = subtotal.add(lineTotal);
            }
        }

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("subtotal", subtotal);

        // Check for badge ID and discount
        String badgeId = (String) session.getAttribute("badgeId");
        if (badgeId != null && !badgeId.isEmpty()) {
            Person person = personBean.findByBadgeId(badgeId);
            if (person != null) {
                BigDecimal discountAmount = personBean.calculateDiscount(subtotal, person);
                BigDecimal total = subtotal.subtract(discountAmount);
                req.setAttribute("person", person);
                req.setAttribute("discountAmount", discountAmount);
                req.setAttribute("total", total);
            } else {
                req.setAttribute("total", subtotal);
                req.setAttribute("badgeError", "Badge ID not found in system.");
            }
        } else {
            req.setAttribute("total", subtotal);
        }

        req.setAttribute("badgeId", badgeId);
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            Long itemId = Long.parseLong(req.getParameter("itemId"));
            MenuItem item = menuBean.findMenuItem(itemId);
            if (item != null && item.isAvailable()) {
                Map<Long, Integer> cart = getCart(session);
                cart.merge(itemId, 1, Integer::sum);
                session.setAttribute("cart", cart);
            }
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        if ("remove".equals(action)) {
            Long itemId = Long.parseLong(req.getParameter("itemId"));
            Map<Long, Integer> cart = getCart(session);
            cart.remove(itemId);
            session.setAttribute("cart", cart);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        if ("update".equals(action)) {
            Long itemId = Long.parseLong(req.getParameter("itemId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            Map<Long, Integer> cart = getCart(session);
            if (quantity <= 0) {
                cart.remove(itemId);
            } else {
                cart.put(itemId, quantity);
            }
            session.setAttribute("cart", cart);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        if ("setBadge".equals(action)) {
            String badgeId = req.getParameter("badgeId");
            session.setAttribute("badgeId", badgeId);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        if ("clear".equals(action)) {
            session.removeAttribute("cart");
            session.removeAttribute("badgeId");
            resp.sendRedirect(req.getContextPath() + "/menu");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
