// Aici implementăm cerința de a calcula discountul (10% student, 15% profesor).

package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.Person;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;

// @Stateless spune serverului GlassFish să creeze și să gestioneze automat această clasă
@Stateless
public class PersonBean {

    // @PersistenceContext injectează automat conexiunea la baza de date (EntityManager)
    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    // Definim procentele de reducere ca niște constante
    public static final BigDecimal STUDENT_DISCOUNT = new BigDecimal("0.10");
    public static final BigDecimal PROFESSOR_DISCOUNT = new BigDecimal("0.15");

    // Metodă pentru a căuta o persoană după ID-ul de pe legitimație (Badge ID)
    public Person findByBadgeID(String badgeId) {
        if (badgeId == null || badgeId.trim().isEmpty()) {
            return null;
        }

        // Căutăm în baza de date persoana cu acel badge
        List<Person> results = em.createQuery("SELECT p FROM Person WHERE p.badgeId = :bid", Person.class)
                .setParameter("bid", badgeId.trim())
                .getResultList();

        // Dacă am găsit-o o returnăm, altfel returnăm null
        return results.isEmpty() ? null : results.get(0);
    }

    // Metoda care calculează reducerea efectivă din prețul total
    public BigDecimal calculateDiscount(BigDecimal total, Person person) {
        if (person == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // Alegem procentul corect în funcție de tipul persoanei
        BigDecimal rate = (person.getType() == Person.PersonType.STUDENT) ? STUDENT_DISCOUNT : PROFESSOR_DISCOUNT;

        // Înmulțim totalul cu rata și păstrăm doar 2 zecimale (ex: 12.50)
        return total.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
