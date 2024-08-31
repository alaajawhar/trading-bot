package com.amdose.database.entities;

import com.amdose.database.enums.ActionStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Getter
@Setter
@Entity
@Table(name = "TRADING_ACTION")
public class ActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "TRADING_SIGNAL_ID")
    private SignalEntity signal;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "BROKER_REQUEST")
    private String brokerRequest;

    @Column(name = "BROKER_RESPONSE")
    private String brokerResponse;

    @Column(name = "ERROR")
    private String error;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ActionStatusEnum status;

    @Column(name = "ADDED_DATE")
    private Date addedDate;
}
