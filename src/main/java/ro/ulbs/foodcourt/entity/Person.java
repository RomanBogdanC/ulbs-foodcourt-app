package ro.ulbs.foodcourt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {

    public enum PersonType {
        STUDENT, PROFESSOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String badgeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonType type;

    public Person() {
    }

    public Person(String badgeId, PersonType type) {
        this.badgeId = badgeId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public PersonType getType() {
        return type;
    }

    public void setType(PersonType type) {
        this.type = type;
    }
}
