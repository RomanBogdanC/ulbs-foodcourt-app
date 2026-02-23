// Aici putem adăuga pe viitor tot felul de rapoarte. Momentan, calculăm totalul încasărilor.

package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

@Stateless
public class StatisticsBean {

    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    // Metodă care adună prețul tuturor comenzilor finalizate
    public BigDecimal getTotalRevenue() {
        BigDecimal total = em.createQuery("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED'", BigDecimal.class)
                .getSingleResult();

        // Dacă nu există nicio comandă, returnăm 0 în loc de null
        return total != null ? total : BigDecimal.ZERO;
    }
}