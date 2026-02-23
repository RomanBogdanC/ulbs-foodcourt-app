package ro.ulbs.foodcourt.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // precision = 10 (cifre în total), scale = 2 (zecimale). Ex: 99999999.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    // Relație de tip N-la-1 (Mai multe produse pot aparține de o singură categorie)
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public MenuItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() {return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Category category() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
