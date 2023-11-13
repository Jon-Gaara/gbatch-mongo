package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Shipping implements Serializable {
    private static final long serialVersionUID = 1L;
    @Field("is_hazmat")
    private String isHazmat;
    @Field("bopis_minimum_order_quantity")
    private String bopisMinimumOrderQuantity;
    @Field("bopis_step_quantity")
    private String bopisStepQuantity;
    @Field("free_shipping_eligible")
    private String freeShippingEligible;
    @Field("ship_alone")
    private String shipAlone;
    @Field("ground_ship_only")
    private String groundShipOnly;
    @Field("restrict_AK_HI_ship")
    private String restrictAkHiShip;
    @Field("ship_minimum_order_quantity")
    private String shipMinimumOrderQuantity;
    @Field("ship_step_quantity")
    private String shipStepQuantity;
    @Field("wcms_status")
    private String wcmsStatus;
    @Field("ecomm_ready_to_ship")
    private String ecommReadyToShip;
    @Field("override_shipping_rate")
    private Boolean overrideShippingRate;
    @Field("standard_rate")
    private BigDecimal standardRate;
    @Field("expedited_rate")
    private BigDecimal expeditedRate;
    @Field("ltl_freight_rate")
    private BigDecimal ltlFreightRate;
    @Field("free_standard_shipping")
    private Boolean freeStandardShipping;
    @Field("shipping_type")
    private Integer shippingType;
    @Field("handling_rate")
    private BigDecimal handlingRate;
    @Field("ship_as_it_is")
    private String shipAsItIs;
    @Field("delivery_method")
    private String deliveryMethod;
}
