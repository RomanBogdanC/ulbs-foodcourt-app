package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
public class AuthBean {

    @PersistenceContext(unitName = "canteenPU")
    private EntityManager em;

    /**
     * Authenticate user with username and password.
     * 
     * @return User if credentials are valid, null otherwise.
     */
    public User authenticate(String username, String password) {
        String hash = hashPassword(password);
        List<User> results = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.passwordHash = :hash", User.class)
                .setParameter("username", username)
                .setParameter("hash", hash)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Simple SHA-256 hashing for passwords.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
