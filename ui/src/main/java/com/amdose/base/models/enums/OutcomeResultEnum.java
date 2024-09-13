package com.amdose.base.models.enums;

import lombok.Getter;

/**
 * @author Alaa Jawhar
 */
@Getter
public enum OutcomeResultEnum {
    WIN("Win"), LOSE("Lose");

    private String label;

    OutcomeResultEnum(String label) {
        this.label = label;
    }

}
