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
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Media implements Serializable {
    private static final long serialVersionUID = 1L;
    @Field("media_name")
    private String mediaName;
    @Field("media_id")
    private String mediaId;
    @Field("media_description")
    private String mediaDescription;
    @Field("media_section")
    private String mediaSection;
    @Field("media_size_id")
    private String mediaSizeId;
    @Field("mime_type_id")
    private String mimeTypeId;
    @URL
    @Field("small_image_url")
    private String smallImageUrl;
    @URL
    @Field("color_image_url")
    private String colorImageUrl;
    @URL
    @Field("swatch_image_url")
    private String swatchImageUrl;
    @Field("is_hero")
    private String isHero;
    @Field("sku_number")
    private String skuNumber;
    @Field("media_type")
    private int mediaType;
    @Field("sort")
    private Integer sort;
    @Field("created_time")
    private LocalDateTime createdTime;
    @URL
    @Field("thumbnail_uri")
    private String thumbnailUri;
    @URL
    @Field("medium_uri")
    private String mediumUri;
    @URL
    @Field("fullsize_uri")
    private String fullSizeUri;
    @URL
    @Field("fullsize_url")
    private String fullSizeUrl;
    @URL
    @Field("thumbnail_url")
    private String thumbnailUrl;
    @URL
    @Field("medium_url")
    private String mediumUrl;
    @URL
    @Field("large_url")
    private String largeUrl;
    @URL
    @Field("video_url")
    private String videoUrl;
}
