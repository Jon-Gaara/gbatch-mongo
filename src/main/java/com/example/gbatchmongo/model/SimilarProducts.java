package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimilarProducts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Field("ca_version_sku_of_same_item")
    private String caVersionSkuOfSameItem;
    @Field("sample_sku")
    private String sampleSku;
    @Field("sample_sku_button_title")
    private String sampleSkuButtonTitle;
    @Field("related_skus")
    private String relatedSkus;
    @Field("like_sku")
    private String likeSku;
    @Field("bulk_alternate_sku")
    private String bulkAlternateSku;
    @Field("in_store_alternate_sku")
    private String inStoreAlternateSku;
    @Field("link_product")
    private String linkProduct;

}
