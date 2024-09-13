package com.amdose.database.entities;

import com.amdose.database.enums.SignalActionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Getter
@Setter
@Entity
@Table(name = "TRADING_SIGNAL")
public class SignalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DETECTION_ID")
    private String detectionId;

    @ManyToOne
    @JoinColumn(name = "BOT_ID")
    private BotEntity bot;

    @Column(name = "META_DATA")
    private String metaData;

    @Column(name = "SCHEDULED_AT")
    private Date scheduledAt;

    @Column(name = "ACTION")
    @Enumerated(EnumType.STRING)
    private SignalActionEnum action;

    @Column(name = "RISK")
    private Double risk;

    @CreationTimestamp
    @Column(name = "ADDED_DATE")
    private Date addedDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRADING_SIGNAL_ID")
    private List<ActionEntity> actionTaken;
}
