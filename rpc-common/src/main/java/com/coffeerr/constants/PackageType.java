package com.coffeerr.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum PackageType {

    REQUEST_PACK(1),
    RESPONSE_PACK(2);
    private int packageCode;
}
