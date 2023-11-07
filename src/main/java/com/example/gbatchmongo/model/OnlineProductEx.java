package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"productId"})
@Document(collection = "online_product")
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineProductEx implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Schema(hidden = true)
    private String productId;

    @Field("sku_number")
    private String skuNumber;

    // FGM remove?
    @Field(name = "sku_name")
    private String skuName;

    @Field(name = "sku_original_name")
    private String skuOriginalName;

    @Field(name = "item_name")
    private String itemName;

    @Field("item_number")
    private String itemNumber;

    @Field("master_sku_number")
    private String masterSkuNumber;
}