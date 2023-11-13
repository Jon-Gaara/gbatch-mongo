package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubSkuWithColor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("sku_number")
    private String skuNumber;
    @Field("color_name")
    private String colorName;
    @URL
    @Field("swatch_image_url")
    private String swatchImageUrl;
    @URL
    @Field("thumbnail_url")
    private String thumbnailUrl;
    @URL
    @Field("fullsize_url")
    private String fullSizeUrl;

    @Field("shipping")
    private Shipping shipping;

}
