package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.Person;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Stateless
public class PersonBean {

    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    public static final BigDecimal STUDENT_DISCOUNT = new BigDecimal("0.10");
    public static final BigDecimal PROFESSOR_DISCOUNT = new BigDecimal("0.15");

    public Person findByBadgeId(String badgeId) {
        if (badgeId == null || badgeId.trim().isEmpty()) {
            return null;
        }

        List<Person> results = em.createQuery("SELECT p FROM Person p WHERE p.badgeId = :bid", Person.class)
                .setParameter("bid", badgeId.trim())
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    public BigDecimal calculateDiscount(BigDecimal total, Person person) {
        if (person == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal rate = (person.getType() == Person.PersonType.STUDENT) ? STUDENT_DISCOUNT : PROFESSOR_DISCOUNT;
        return total.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}