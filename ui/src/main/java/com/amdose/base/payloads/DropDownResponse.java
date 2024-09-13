package com.amdose.base.payloads;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class DropDownResponse {
    List<KeyValueItem> list = new ArrayList<>();

    public void addKeyValueItem(KeyValueItem item) {
        this.list.add(item);
    }
}
