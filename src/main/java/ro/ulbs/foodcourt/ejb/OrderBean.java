// Această clasă preia comanda de la Kiosk, o salvează și permite bucătăriei să îi schimbe statusul.

package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class OrderBean {

    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    // Metodă pentru plasarea unei noi comenzi de la Kiosk
    public void placeOrder(Order order) {
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PLACED"); // Status inițial
        em.persist(order); // Salvează comanda
    }

    // Metodă folosită de ecranul din Bucătărie pentru a vedea comenzile în așteptare
    public List<Order> getActiveOrders() {
        return em.createQuery("SELECT o FROM Order o WHERE o.status IN ('PLACED', 'PREPARING') ORDER BY o.orderTime ASC", Order.class)
                .getResultList();
    }

    // Metodă folosită de Bucătărie pentru a marca o comandă ca "PREPARING" sau "READY"
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = em.find(Order.class, orderId);
        if (order != null) {
            order.setStatus(newStatus);
            em.merge(order); // Actualizează rândul în baza de date
        }
    }
}