// Această clasă se va ocupa de extragerea și salvarea produselor în baza de date.

package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.Category;
import ro.ulbs.foodcourt.entity.MenuItem;

import java.util.List;

// @Stateless spune serverului GlassFish să creeze și să gestioneze automat această clasă
@Stateless
public class MenuBean {

    // @PersistenceContext injectează automat conexiunea la baza de date (EntityManager)
    @PersistenceContext(unitName = "foodcourtPU")
    private EntityManager em;

    // Metodă care aduce toate categoriile din baza de date
    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();

    }

    // Metodă care aduce toate produsele, împreună cu categoria lor
    public List<MenuItem> getAllMenuItems() {
        return em.createQuery("SELECT m FROM MenuItem m JOIN FETCH m.category ORDER BY m.category.name, m.name", MenuItem.class).getResultList();
    }

    // Metodă pentru a găsi un produs specific după ID-ul său
    public MenuItem findMenuItem(Long id) {
        return em.find(MenuItem.class, id);
    }

    // Metodă pentru a adăuga o categorie nouă în baza de date
    public void addCategory(Category category) {
        em.persist(category);
    }

    // Metodă pentru a adăuga un produs nou în baza de date
    public void addMenuItem(MenuItem item) {
        em.persist(item);
    }
}
