package com.amdose.database.entities;

import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.enums.TimeFrameEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Getter
@Setter
@Entity
@Table(name = "BOT")
public class BotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SYMBOL_ID")
    private SymbolEntity symbol;

    @ManyToOne
    @JoinColumn(name = "STRATEGY_ID")
    private StrategyEntity strategy;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BOT_MODE")
    @Enumerated(EnumType.STRING)
    private BotModeEnum mode;

    @Column(name = "TIME_FRAME")
    @Enumerated(EnumType.STRING)
    private TimeFrameEnum timeFrame;

    @Column(name = "ADDED_BY")
    private String addedBy;

    @CreationTimestamp
    @Column(name = "ADDED_DATE")
    private Date addedDate;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
}
