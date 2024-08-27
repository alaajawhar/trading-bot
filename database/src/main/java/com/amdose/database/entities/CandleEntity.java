package com.amdose.database.entities;

import com.amdose.database.enums.TimeFrameEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Getter
@Setter
@Entity
@Table(name = "CANDLE")
public class CandleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SYMBOL_ID")
    private SymbolEntity symbol;

    @Column(name = "CANDLE_DATE")
    private Date date;

    @Column(name = "OPEN")
    private double open;

    @Column(name = "HIGH")
    private double high;

    @Column(name = "LOW")
    private double low;

    @Column(name = "CLOSE")
    private double close;

    @Column(name = "VOLUME")
    private double volume;

    @Column(name = "TIME_FRAME")
    @Enumerated(EnumType.STRING)
    private TimeFrameEnum timeFrame;
}
