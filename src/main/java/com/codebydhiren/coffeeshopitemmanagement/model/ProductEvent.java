package com.codebydhiren.coffeeshopitemmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString(callSuper = false)
public class ProductEvent {

    private String uuid;
    private Instant timeNow;
    private Long itemID;

}
