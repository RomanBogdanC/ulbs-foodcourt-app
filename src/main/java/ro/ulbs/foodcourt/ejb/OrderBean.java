package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Stateless
public class OrderBean {

    @PersistenceContext(unitName = "canteenPU")
    private EntityManager em;

    @EJB
    private PersonBean personBean;

    @EJB
    private MenuBean menuBean;

    /**
     * Create and persist an order.
     * 
     * @param cartItems map of menuItemId -> quantity
     * @param badgeId   optional badge ID for discount
     * @return the created Order
     */
    public Order createOrder(Map<Long, Integer> cartItems, String badgeId) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Look up person for discount
        Person person = personBean.findByBadgeId(badgeId);

        Order order = new Order();
        order.setDateTime(LocalDateTime.now());
        order.setStatus("COMPLETED");
        order.setPerson(person);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long itemId = entry.getKey();
            int qty = entry.getValue();

            MenuItem menuItem = menuBean.findMenuItem(itemId);
            if (menuItem == null) {
                throw new IllegalArgumentException("Menu item not found: " + itemId);
            }
            if (menuItem.getStock() < qty) {
                throw new IllegalStateException("Insufficient stock for: " + menuItem.getName());
            }

            // Decrease stock
            menuItem.setStock(menuItem.getStock() - qty);
            em.merge(menuItem);

            // Create order item
            OrderItem oi = new OrderItem(order, menuItem, qty, menuItem.getPrice());
            order.getItems().add(oi);

            subtotal = subtotal.add(menuItem.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        // Calculate discount
        BigDecimal discountAmount = personBean.calculateDiscount(subtotal, person);
        BigDecimal totalPrice = subtotal.subtract(discountAmount);

        order.setDiscountAmount(discountAmount);
        order.setTotalPrice(totalPrice);

        em.persist(order);
        return order;
    }

    public Order findOrder(Long id) {
        return em.find(Order.class, id);
    }
}
