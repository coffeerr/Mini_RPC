package com.coffeerr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: asd
 * @author: Desmond
 * @time: 2022/4/7 9:05 上午
 */
@Data
@AllArgsConstructor
public class HelloObject implements Serializable {
    private Integer ID;
    private String message;
}
