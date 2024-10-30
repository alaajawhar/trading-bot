package com.amdose.pattern.detection.dtos;

import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.SignalActionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalItemDTO {
    private String detectionId;
    private String metaData;
    private Date scheduledAt;
    private SignalActionEnum action;
    private Double risk;

    public SignalEntity toSignalEntity() {
        SignalEntity signalEntity = new SignalEntity();
        signalEntity.setDetectionId(this.getDetectionId());
        signalEntity.setMetaData(this.getMetaData());
        signalEntity.setScheduledAt(this.getScheduledAt());
        signalEntity.setAction(this.getAction());
        signalEntity.setRisk(this.getRisk());
        return signalEntity;
    }
}
