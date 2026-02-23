package ro.ulbs.foodcourt.servlet;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.transaction.UserTransaction;
import ro.ulbs.foodcourt.ejb.AuthBean;
import ro.ulbs.foodcourt.entity.Category;
import ro.ulbs.foodcourt.entity.MenuItem;
import ro.ulbs.foodcourt.entity.User;

import java.math.BigDecimal;

@WebListener
public class DataInitializer implements ServletContextListener {

    @PersistenceContext(unitName = "canteenPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            utx.begin();

            // Create default admin user (password: admin123)
            User admin = new User("admin", AuthBean.hashPassword("admin123"), User.Role.MANAGER);
            em.persist(admin);

            User kitchen = new User("kitchen", AuthBean.hashPassword("kitchen123"), User.Role.KITCHEN);
            em.persist(kitchen);

            // Create sample categories and items
            Category meniuri = new Category("Meniuri");
            em.persist(meniuri);
            Category bauturi = new Category("Bauturi");
            em.persist(bauturi);
            Category desert = new Category("Desert");
            em.persist(desert);
            Category supe = new Category("Supe & Ciorbe");
            em.persist(supe);

            em.persist(new MenuItem("Meniu Snitzel", new BigDecimal("25.99"),
                    "Snitzel de pui cu cartofi prajiti si salata", 50, meniuri));
            em.persist(new MenuItem("Meniu Paste Carbonara", new BigDecimal("22.50"),
                    "Paste cu sos carbonara, parmezan si bacon", 40, meniuri));
            em.persist(new MenuItem("Meniu Gratar Mixt", new BigDecimal("32.00"), "Gratar mixt cu garnitura la alegere",
                    30, meniuri));

            em.persist(new MenuItem("Coca-Cola 330ml", new BigDecimal("5.00"), "Bautura racoritoare", 100, bauturi));
            em.persist(new MenuItem("Apa Plata 500ml", new BigDecimal("3.00"), "Apa minerala naturala", 150, bauturi));
            em.persist(new MenuItem("Suc Natural Portocale", new BigDecimal("8.50"), "Suc proaspat stors de portocale",
                    30, bauturi));

            em.persist(new MenuItem("Papanasi cu Smantana", new BigDecimal("15.00"),
                    "Papanasi traditionali cu smantana si dulceata", 25, desert));
            em.persist(new MenuItem("Clatite cu Nutella", new BigDecimal("12.00"),
                    "Clatite cu Nutella si fructe de padure", 35, desert));

            em.persist(
                    new MenuItem("Ciorba de Burta", new BigDecimal("14.00"), "Ciorba de burta traditionala", 20, supe));
            em.persist(new MenuItem("Supa Crema de Legume", new BigDecimal("10.00"),
                    "Supa crema cu legume de sezon si crutoane", 25, supe));

            // Create sample persons
            em.persist(new ro.ulbs.foodcourt.entity.Person("STU001", ro.ulbs.foodcourt.entity.Person.PersonType.STUDENT));
            em.persist(new ro.ulbs.foodcourt.entity.Person("STU002", ro.ulbs.foodcourt.entity.Person.PersonType.STUDENT));
            em.persist(
                    new ro.ulbs.foodcourt.entity.Person("PROF001", ro.ulbs.foodcourt.entity.Person.PersonType.PROFESSOR));
            em.persist(
                    new ro.ulbs.foodcourt.entity.Person("PROF002", ro.ulbs.foodcourt.entity.Person.PersonType.PROFESSOR));

            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
