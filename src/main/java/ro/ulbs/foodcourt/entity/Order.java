package ro.ulbs.foodcourt.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // Relație N-la-1 (O persoană poate avea mai multe comenzi)
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    private BigDecimal getTotalPrice() { return totalPrice; }
    private void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    private Person getPerson() { return person; }
    private void setPerson(Person person) { this.person = person; }
}
