package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;
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
    @Schema(
            hidden = true
    )
    private String productId;
    @Field("sku_number")
    private String skuNumber;
    @Field(
            name = "sku_name"
    )
    private String skuName;
    @Field(
            name = "sku_original_name"
    )
    private String skuOriginalName;
    @Field(
            name = "item_name"
    )
    private String itemName;
    @Field("item_number")
    private String itemNumber;
    @Field(
            name = "sku_display_name"
    )
    private String skuDisplayName;
    @Field("sku_type")
    private Integer skuType;
    @Field("sku_url")
    private String skuUrl;
    @Field("taxonomy_id")
    private Long taxonomyId;
    @Field("taxonomy_long_name")
    private String taxonomyLongName;
    @Field("taxonomy_external_id")
    private String taxonomyExternalId;
    @Field("taxonomy_external_id_path")
    private String taxonomyExternalIdPath;
    @Field("parent_taxonomy_path")
    private String parentTaxonomyPath;
    @Field("full_taxonomy_path")
    private String fullTaxonomyPath;
    @Field("global_trade_item_number")
    private String globalTradeItemNumber;
    @Field("seller_sku_number")
    private String sellerSkuNumber;
    @Field("renew_option")
    private Integer renewOption;
    @Field("sub_skus")
    private List<SubSku> subSkus;
    @Field("enable_personalization")
    private boolean enablePersonalization;
    @Field("category_id_list")
    private List<Long> categoryIdList;
    @Field("category_path_list")
    private List<String> categoryPathList;
    @Field("master_sku_number")
    private String masterSkuNumber;
    @Field("short_description")
    private String shortDescription;
    @Field("medium_description")
    private String mediumDescription;
    @Field("long_description")
    private String longDescription;
    @Field("consumer_friendly_description")
    private String consumerFriendlyDescription;
    @Field("materials_description")
    private String materialsDescription;
    @Field("featured")
    private Boolean featured;
    @Field("status")
    private Integer status;
    @Field("last_status")
    private Integer lastStatus;
    @Field("new_flag_date")
    private Date newFlagDate;
    @CreatedBy
    @JsonSerialize(
            using = ToStringSerializer.class
    )
    @Field(
            name = "created_by"
    )
    private Long createdBy;
    @Field("create_time")
    @CreatedDate
    private LocalDateTime createdTime;
    @Field("publish_time")
    private LocalDateTime publishTime;
    @Field("first_publish_time")
    private LocalDateTime firstPublishTime;
    @LastModifiedBy
    @JsonSerialize(
            using = ToStringSerializer.class
    )
    @Field(
            name = "modified_by"
    )
    private Long modifiedBy;
    @LastModifiedDate
    @Field(
            name = "update_time"
    )
    private LocalDateTime updateDatetime;
    @JsonSerialize(
            using = ToStringSerializer.class
    )
    @Field(
            name = "seller_store_id"
    )
    private Long sellerStoreId;
    @Field(
            name = "time_frame"
    )
    private String timeFrame;
    @Field("lead_time")
    private String leadTime;
    @Field("who_made")
    private String whoMade;
    @Field("what_is")
    private String whatIs;
    @Field("when_made")
    private String whenMade;
    @Field("price")
    private BigDecimal price;
    @Field("item_price")
    private ItemPrice itemPrice;
    @Field(
            name = "draft_inventory"
    )
    private Long inventory;
    @Field(
            name = "store_name"
    )
    private String storeName;
    @Field(
            name = "store_city"
    )
    private String storeCity;
    @Field(
            name = "store_state"
    )
    private String storeState;
    @Field(
            name = "store_country"
    )
    private String storeCountry;
    @Field("overall_rating")
    private Double overallRating;
    @Field("comment_count")
    private Long commentCount;
    @Field("bought_count")
    private Long boughtCount;
    @URL
    @Field("thumbnail_url")
    private String thumbnailUrl;
    @Field("upc_number")
    private String upcNumber;
    @Field("category_id")
    private Long categoryId;
    @Field("category_path")
    private String categoryPath;
    @Field(
            name = "taxonomy_ids"
    )
    private List<Long> taxonomyIdList;
    @Field(
            name = "taxonomy_paths"
    )
    private List<String> taxonomyPathList;
    @Field("tags")
    private List<String> tags;
    @Field("keyphrases")
    private List<String> keyPhrases;
    @Field("is_primary_sku")
    private Boolean isPrimarySku;
    @Field("is_assorted")
    private Boolean isAssorted;
    @Field("item_type")
    private String itemType;
    @Field("sku_group")
    private String skuGroup;
    @Field("digital_asset")
    private Boolean digitalAsset;
    @Field("is_finished_goods")
    private Boolean isFinishedGoods;
    /** @deprecated */
    @Deprecated
    @Field("is_third_party_product")
    private Boolean isThirdPartyProduct;
    @Field("is_online_only")
    private Boolean isOnlineOnly;
    @Field("brand_segmentation")
    private Boolean brandSegmentation;
    @Field("seasonal_item")
    private Boolean seasonalItem;
    @Field("materials_product_list")
    private List<String> materialsProductList;
    @Field("expired_time")
    private LocalDateTime expiredTime;
    @Field("inventory_id")
    private String inventoryId;
    @Field("online_inventory_available")
    private Boolean onlineInventoryAvailable;
    @Field("return_policy_option")
    private Integer returnPolicyOption;
    @Field(
            name = "override_shipping_return_policy"
    )
    private Boolean overrideShippingReturnPolicy;
    @Field("refund_only")
    private Boolean refundOnly;
    @JsonSerialize(
            using = ToStringSerializer.class
    )
    @Field("inventory_master")
    private Long inventoryMaster;
    @Field("default_sub_sku")
    private String defaultSubSku;
    @Field("manual")
    private String manual;
    @Field("is_subscribeable")
    private Boolean isSubscribeable;
    @Field("percent_off_on_price")
    private Integer percentOffOnPrice;
    @Field("percent_off_on_repeat_deliveries")
    private Integer percentOffOnRepeatDeliveries;
    @Field("variants")
    private List<Variant> variants;
    @Field("filtered_variants")
    private List<Variant> filteredVariants;
    @Field(
            name = "media"
    )
    private List<Media> media;
    @Field("similar_products")
    private SimilarProducts similarProducts;
    @Field("store_only")
    private Boolean storeOnly;
    @Field("item_properties")
    private ItemProperties itemProperties;
    @Field("everyday_value_flag")
    private Boolean everydayValueFlag;
    @Field("holiday_item")
    private Boolean holidayItem;
    @Field("dynamic_attributes")
    private Map<String, String> dynamicAttributes;
    @Field("search_properties")
    private Map<String, String> searchProperties;
    @Field("mik_overall_item_status")
    private String mikOverallItemStatus;
    @Field("mik_age_ver_req_ind")
    private String mikAgeVerReqInd;
    @Field("valuable")
    private String valuable;
    @Field("eas_security_tag")
    private String easSecurityTag;
    @Field("coupon_eligible")
    private Boolean couponEligible;
    @Field("recommended_age_range")
    private String recommendedAgeRange;
    @Field("taxonomy_external_id_paths")
    private List<String> taxonomyExternalIdPaths;
    @Field("taxonomy_external_ids")
    private List<String> taxonomyExternalIds;
    @Field(
            name = "cancel_accepted_period"
    )
    private int cancelAcceptedPeriod = 1;
    @Field(
            name = "return_accepted_period"
    )
    private int returnAcceptedPeriod = 0;
    @Field(
            name = "is_bulk"
    )
    private Boolean isBulk;
    @Field(
            name = "url_taxonomy_path"
    )
    private String urlTaxonomyPath;
    @Field(
            name = "inactive_log"
    )
    private Map<String, Object> inactiveLog;
    @Field(
            name = "is_commissioned"
    )
    private Boolean isCommissioned;
    @Field(
            name = "online_flag"
    )
    private Boolean onlineFlag;
    @Field(
            name = "searchable_if_unavailable"
    )
    private Boolean searchableIfUnavailable;
    @Field(
            name = "searchable"
    )
    private Boolean searchable = true;
    @Field(
            name = "mik_strategy_code"
    )
    private Integer mikStrategyCode;
    @Field(
            name = "full_menu_path"
    )
    private String fullMenuPath;
    @Field(
            name = "url_menu_path"
    )
    private String urlMenuPath;
    @Field(
            name = "menu_external_id"
    )
    private String menuExternalId;
    @Field(
            name = "menu_external_id_path"
    )
    private String menuExternalIdPath;
    @Field("menu_external_id_paths")
    private List<String> menuExternalIdPaths;
    @Field(
            name = "variant_skus"
    )
    private Map<String, String> variantSkus;
    @Field(
            name = "variant_count_map"
    )
    private Map<String, Integer> variantCountMap;
    @Field(
            name = "variant_attr"
    )
    private String variantAttr;
    @Field(
            name = "free_title_info"
    )
    private String freeTitleInfo;
    @Field(
            name = "sub_skus_with_color"
    )
    private List<SubSkuWithColor> subSkuWithColor;
    @Field(
            name = "mik_price_prompt_ind"
    )
    private String mikPricePromptInd;
    @Field(
            name = "card_value_range"
    )
    private String cardValueRange;
    @Field(
            name = "ea_sku_number_long"
    )
    private Long eaSkuNumberLong;
    @Field(
            name = "upc_available"
    )
    private Boolean upcAvailable;

    @Field("search_properties_v2")
    private Map<String, Map<String, String>> searchPropertiesV2;

    @Field("shipping")
    private Shipping shipping;

    @Field("channel")
    private Integer channel;

}