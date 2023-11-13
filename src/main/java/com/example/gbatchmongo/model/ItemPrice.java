package com.example.gbatchmongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemPrice {
    @Field("price")
    private String price;
    @Field("origin_price")
    private String orgPrice;
    @Field("price_range")
    private String priceRange;
    @Field("origin_price_range")
    private String orgPriceRange;
    @Field("unit_price")
    private String unitPrice;
    @Field("unit_price_range")
    private String unitPriceRange;
    @Field("price_flag")
    private String priceFlag;
    @Field("saved_amount")
    private String savedAmount;
    @Field("saved_percent")
    private String savedPercent;
    @Field("number_price")
    private String numberPrice;
}
