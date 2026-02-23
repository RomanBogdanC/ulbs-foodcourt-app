package ro.ulbs.foodcourt.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.ulbs.foodcourt.ejb.StatisticsBean;
import ro.ulbs.foodcourt.entity.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "StatisticsServlet", urlPatterns = { "/admin/orders", "/admin/statistics" })
public class StatisticsServlet extends HttpServlet {

    @EJB
    private StatisticsBean statisticsBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/admin/orders".equals(path)) {
            handleOrders(req, resp);
        } else if ("/admin/statistics".equals(path)) {
            handleStatistics(req, resp);
        }
    }

    private void handleOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Parse parameters
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }

        boolean ascending = true;
        if ("desc".equals(req.getParameter("sort"))) {
            ascending = false;
        }

        LocalDate filterDate = null;
        String dateParam = req.getParameter("filterDate");
        if (dateParam != null && !dateParam.isEmpty()) {
            filterDate = LocalDate.parse(dateParam);
        }

        int pageSize = 10;
        List<Order> orders = statisticsBean.getOrders(page, pageSize, ascending, filterDate);
        long totalOrders = statisticsBean.countOrders(filterDate);
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        req.setAttribute("orders", orders);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("ascending", ascending);
        req.setAttribute("filterDate", dateParam);

        // Daily total sales
        if (filterDate != null) {
            BigDecimal dailyTotal = statisticsBean.getTotalSalesForDate(filterDate);
            req.setAttribute("dailyTotal", dailyTotal);
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
    }

    private void handleStatistics(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fromParam = req.getParameter("fromDate");
        String toParam = req.getParameter("toDate");
        String badgeId = req.getParameter("badgeId");

        if (fromParam != null && !fromParam.isEmpty() && toParam != null && !toParam.isEmpty()) {
            LocalDate fromDate = LocalDate.parse(fromParam);
            LocalDate toDate = LocalDate.parse(toParam);

            // Per-product stats
            List<Object[]> productStats = statisticsBean.getProductStats(fromDate, toDate);
            req.setAttribute("productStats", productStats);

            // Per badge ID stats
            if (badgeId != null && !badgeId.isEmpty()) {
                List<Order> badgeOrders = statisticsBean.getOrdersByBadgeId(badgeId, fromDate, toDate);
                BigDecimal badgeTotal = statisticsBean.getTotalSpentByBadgeId(badgeId, fromDate, toDate);
                req.setAttribute("badgeOrders", badgeOrders);
                req.setAttribute("badgeTotal", badgeTotal);
                req.setAttribute("badgeId", badgeId);
            }

            req.setAttribute("fromDate", fromParam);
            req.setAttribute("toDate", toParam);
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/statistics.jsp").forward(req, resp);
    }
}
