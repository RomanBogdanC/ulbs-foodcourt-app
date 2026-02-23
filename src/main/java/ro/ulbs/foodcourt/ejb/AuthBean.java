// Verifică dacă un utilizator (Manager sau Bucătar) a introdus datele corecte.

package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.User;

import java.util.List;

@Stateless
public class AuthBean {

    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    // Metodă de login care returnează User-ul dacă datele sunt corecte, sau null dacă sunt greșite
    public User authenticate(String username, String password) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :user AND u.password = :pass", User.class)
                .setParameter("user", username)
                .setParameter("pass", password)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }
}