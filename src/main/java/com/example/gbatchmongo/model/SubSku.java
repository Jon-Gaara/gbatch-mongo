package com.example.gbatchmongo.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("sku_number")
    private String skuNumber;
    @Field("seller_sku_number")
    private String sellerSkuNumber;
    @Field("availability")
    private Boolean availability;
    @Field("status")
    private Integer status;
    @Field("inventory")
    private Long inventory;
    @Field("price")
    private BigDecimal price;
    @Field("attrs")
    private String attrs;
    @Field("similar_products")
    private SimilarProducts similarProducts;

}
