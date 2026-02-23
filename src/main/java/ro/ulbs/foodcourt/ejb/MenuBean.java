package ro.ulbs.foodcourt.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.ulbs.foodcourt.entity.Category;
import ro.ulbs.foodcourt.entity.MenuItem;

import java.util.List;

@Stateless
public class MenuBean {

    @PersistenceContext(unitName = "canteenPU")
    private EntityManager em;

    // ---- Categories ----

    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public Category findCategory(Long id) {
        return em.find(Category.class, id);
    }

    public void addCategory(Category category) {
        em.persist(category);
    }

    public void updateCategory(Category category) {
        em.merge(category);
    }

    public void deleteCategory(Long id) {
        Category c = em.find(Category.class, id);
        if (c != null) {
            em.remove(c);
        }
    }

    // ---- Menu Items ----

    public List<MenuItem> getAllMenuItems() {
        return em
                .createQuery("SELECT m FROM MenuItem m JOIN FETCH m.category ORDER BY m.category.name, m.name",
                        MenuItem.class)
                .getResultList();
    }

    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.category.id = :catId ORDER BY m.name", MenuItem.class)
                .setParameter("catId", categoryId)
                .getResultList();
    }

    public MenuItem findMenuItem(Long id) {
        return em.find(MenuItem.class, id);
    }

    public void addMenuItem(MenuItem item) {
        em.persist(item);
    }

    public void updateMenuItem(MenuItem item) {
        em.merge(item);
    }

    public void deleteMenuItem(Long id) {
        MenuItem m = em.find(MenuItem.class, id);
        if (m != null) {
            em.remove(m);
        }
    }

    public void updateStock(Long itemId, int newStock) {
        MenuItem m = em.find(MenuItem.class, itemId);
        if (m != null) {
            m.setStock(newStock);
            em.merge(m);
        }
    }

    public boolean isAvailable(Long itemId) {
        MenuItem m = em.find(MenuItem.class, itemId);
        return m != null && m.getStock() > 0;
    }
}
