package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ro.ulbs.foodcourt.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class StatisticsBean {

    @PersistenceContext(unitName = "canteenPU")
    private EntityManager em;

    /**
     * Get paginated orders, sorted by dateTime.
     * 
     * @param page       1-based page number
     * @param pageSize   number of results per page
     * @param ascending  true for ASC, false for DESC
     * @param filterDate optional date filter (only orders from that day)
     */
    public List<Order> getOrders(int page, int pageSize, boolean ascending, LocalDate filterDate) {
        String sortDir = ascending ? "ASC" : "DESC";
        String jpql;
        TypedQuery<Order> query;

        if (filterDate != null) {
            LocalDateTime start = filterDate.atStartOfDay();
            LocalDateTime end = filterDate.plusDays(1).atStartOfDay();
            jpql = "SELECT o FROM Order o WHERE o.dateTime >= :start AND o.dateTime < :end ORDER BY o.dateTime "
                    + sortDir;
            query = em.createQuery(jpql, Order.class)
                    .setParameter("start", start)
                    .setParameter("end", end);
        } else {
            jpql = "SELECT o FROM Order o ORDER BY o.dateTime " + sortDir;
            query = em.createQuery(jpql, Order.class);
        }

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    /**
     * Count total orders (optionally filtered by date).
     */
    public long countOrders(LocalDate filterDate) {
        if (filterDate != null) {
            LocalDateTime start = filterDate.atStartOfDay();
            LocalDateTime end = filterDate.plusDays(1).atStartOfDay();
            return em
                    .createQuery("SELECT COUNT(o) FROM Order o WHERE o.dateTime >= :start AND o.dateTime < :end",
                            Long.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();
        }
        return em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
    }

    /**
     * Total sales for a given date.
     */
    public BigDecimal getTotalSalesForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        BigDecimal result = em.createQuery(
                "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.dateTime >= :start AND o.dateTime < :end",
                BigDecimal.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return result;
    }

    /**
     * Per-product statistics for a date range.
     * Returns list of [productName, totalQuantity, totalRevenue].
     */
    public List<Object[]> getProductStats(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.plusDays(1).atStartOfDay();
        return em.createQuery(
                "SELECT oi.menuItem.name, SUM(oi.quantity), SUM(oi.unitPrice * oi.quantity) " +
                        "FROM OrderItem oi WHERE oi.order.dateTime >= :start AND oi.order.dateTime < :end " +
                        "GROUP BY oi.menuItem.name ORDER BY oi.menuItem.name",
                Object[].class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    /**
     * Orders for a specific badge ID within a date range.
     */
    public List<Order> getOrdersByBadgeId(String badgeId, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.plusDays(1).atStartOfDay();
        return em.createQuery(
                "SELECT o FROM Order o WHERE o.person.badgeId = :badgeId " +
                        "AND o.dateTime >= :start AND o.dateTime < :end ORDER BY o.dateTime DESC",
                Order.class)
                .setParameter("badgeId", badgeId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    /**
     * Total spent by a badge ID within a date range.
     */
    public BigDecimal getTotalSpentByBadgeId(String badgeId, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.plusDays(1).atStartOfDay();
        BigDecimal result = em.createQuery(
                "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.person.badgeId = :badgeId " +
                        "AND o.dateTime >= :start AND o.dateTime < :end",
                BigDecimal.class)
                .setParameter("badgeId", badgeId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return result;
    }
}
