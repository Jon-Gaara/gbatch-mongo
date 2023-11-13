package com.example.gbatchmongo.common.enums;

import java.util.Arrays;
import java.util.List;

public enum SkuStatus {
    UNKNOWN(0, "UNKNOWN"),
    ACTIVE(1, "ACTIVE"),
    DRAFT(2, "DRAFT"),
    EXPIRED(3, "EXPIRED"),
    SOLD_OUT(4, "SOLD_OUT"),
    INACTIVE(5, "INACTIVE"),
    ARCHIVED(6, "ARCHIVED"),
    DELETED(7, "DELETED"),
    VARIATION_OUT_OF_STOCK(8, "VARIATION_OUT_OF_STOCK"),
    UNSOLD(9, "UNSOLD"),
    FORBIDDEN(10, "FORBIDDEN"),
    PENDING_REVIEW(11, "PENDING_REVIEW"),
    AWAITING_TAX_CODE_ASSIGNMENT(12, "AWAITING_TAX_CODE_ASSIGNMENT"),
    SUSPENDED(13, "SUSPENDED");

    private final Integer code;
    private final String value;
    private static final List<Integer> codeList = Arrays.asList(UNKNOWN.code, ACTIVE.code, DRAFT.code, EXPIRED.code, SOLD_OUT.code, INACTIVE.code, ARCHIVED.code, DELETED.code, VARIATION_OUT_OF_STOCK.code, UNSOLD.code, FORBIDDEN.code, PENDING_REVIEW.code, AWAITING_TAX_CODE_ASSIGNMENT.code, SUSPENDED.code);
    private static final List<String> valueList = Arrays.asList(UNKNOWN.value, ACTIVE.value, DRAFT.value, EXPIRED.value, SOLD_OUT.value, INACTIVE.value, ARCHIVED.value, DELETED.value, VARIATION_OUT_OF_STOCK.value, UNSOLD.value, FORBIDDEN.value, PENDING_REVIEW.value, AWAITING_TAX_CODE_ASSIGNMENT.value, SUSPENDED.value);

    private SkuStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static boolean containsCode(Integer code) {
        return codeList.contains(code);
    }

    public static boolean containsValue(String value) {
        return valueList.contains(value);
    }

    public static SkuStatus fromInt(int status) {
        SkuStatus[] enumConstants = (SkuStatus[])SkuStatus.class.getEnumConstants();
        SkuStatus[] var2 = enumConstants;
        int var3 = enumConstants.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SkuStatus enumConstant = var2[var4];
            if (enumConstant.getCode() == status) {
                return enumConstant;
            }
        }

        return null;
    }

    public static SkuStatus fromString(String status) {
        SkuStatus[] enumConstants = (SkuStatus[])SkuStatus.class.getEnumConstants();
        SkuStatus[] var2 = enumConstants;
        int var3 = enumConstants.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SkuStatus enumConstant = var2[var4];
            if (enumConstant.getValue().equals(status)) {
                return enumConstant;
            }
        }

        return null;
    }
}
