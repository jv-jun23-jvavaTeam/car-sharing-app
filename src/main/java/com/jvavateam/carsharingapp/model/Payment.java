package com.jvavateam.carsharingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rental_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Rental rental;*/
    @Column(nullable = false, unique = true)
    private String sessionUrl;
    @Column(nullable = false, unique = true)
    private String sessionId;
    @Column(nullable = false)
    private BigDecimal amountToPay;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        PENDING,
        PAID
    }

    public enum Type {
        PAYMENT,
        FINE
    }
}
