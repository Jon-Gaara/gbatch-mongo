package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("variant_id")
    private String variantId;
    @Field("variant_name")
    private String variantName;
    @Field("variant_type")
    private String variantType;
    @Field("variant_uom")
    private List<String> variantUOM;
    @Field("add_images")
    private Boolean addImages;
    @Field("variant_content")
    private List<String> variantContent;
    @Field("variant_swatch_urls")
    private Map<String, String> variantSwatchUrls;
}
