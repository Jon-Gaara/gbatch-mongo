package com.example.gbatchmongo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Field("country_of_origin")
    private String countryOfOrigin;
    @Field("country_of_origin_id")
    private String countryOfOriginId;
    @Field("brand_bucket")
    private String brandBucket;
    @Field("brand_name")
    private String brandName;
    @Field("sub_brand")
    private String subBrand;
    @Field("nbe_equivalent")
    private String nbeEquivalent;
    @Field("licensed_character")
    private String licensedCharacter;
    @Field("open_stock_set_kit")
    private String openStockSetKit;
    @Field("theme")
    private String theme;
    @Field("occasion")
    private String occasion;
    @Field("personalizable")
    private String personalizable;
    @Field("backorder_able")
    private String backorderAble;
    @Field("difficulty_level")
    private String difficultyLevel;
    @Field("material")
    private String material;
    @Field("scent")
    private String scent;
    @Field("stamp_type")
    private String stampType;
    @Field("adhesive_type")
    private String adhesiveType;
    @Field("solubility")
    private String solubility;
    @Field("template_type")
    private String templateType;
    @Field("intended_use_type")
    private String intendedUseType;
    @Field("usage_and_care")
    private String usageAndCare;
    @Field("is_plant")
    private Boolean isPlant;
    @Field("flower_plant_type")
    private String flowerPlantType;
    @Field("garment_type")
    private String garmentType;
    @Field("gender")
    private String gender;
    @Field("is_ribbon")
    private Boolean isRibbon;
    @Field("ribbon_style_name")
    private String ribbonStyleName;
    @Field("ribbon_type_id")
    private String ribbonTypeId;
    @Field("total_count_in_unit")
    private String totalCountInUnit;
    @Field("hex_color")
    private String hexColor;
    @Field("color_code")
    private String colorCode;
    @Field("color_family")
    private String colorFamily;
    @Field("color_name")
    private String colorName;
    @Field("shape")
    private String shape;
    @Field("length")
    private String length;
    @Field("length_uom")
    private String lengthUom;
    @Field("width")
    private String width;
    @Field("width_uom")
    private String widthUom;
    @Field("height")
    private String height;
    @Field("height_uom")
    private String heightUom;
    @Field("depth")
    private String depth;
    @Field("depth_uom")
    private String depthUom;
    @Field("weight")
    private String weight;
    @Field("weight_uom")
    private String weightUom;
    @Field("capacity")
    private String capacity;
    @Field("capacity_uom")
    private String capacityUom;
    @Field("volume")
    private String volume;
    @Field("volume_uom")
    private String volumeUom;
    @Field("size")
    private String size;
    @Field("size_uom")
    private String sizeUom;
    @Field("framed_size")
    private String framedSize;
    @Field("matted_size")
    private String mattedSize;
    @Field("yarn_weight")
    private String yarnWeight;
    @Field("features")
    private String features;
    @Field("indoor_outdoor")
    private String indoorOutdoor;
    @Field("light_bulb_color")
    private String lightBulbColor;
    @Field("light_bulb_type")
    private String lightBulbType;
    @Field("paint_type")
    private String paintType;
    @Field("pencil_grade")
    private String pencilGrade;
    @Field("number_of_openings")
    private String numberOfOpenings;
    @Field("wash_care_instructions")
    private String washCareInstructions;
    @Field("initial_markup")
    private String initialMarkup;
    @Field("dynamic_properties")
    private Map<String, String> dynamicProperties;
}
