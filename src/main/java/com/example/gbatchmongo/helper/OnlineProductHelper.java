package com.example.gbatchmongo.helper;

import com.example.gbatchmongo.common.enums.ChannelType;
import com.example.gbatchmongo.common.enums.SkuStatus;
import com.example.gbatchmongo.common.enums.SkuType;
import com.example.gbatchmongo.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnlineProductHelper {

    public static void fillSubSkuWithColorAndVariant(OnlineProductEx masterOnlineProduct, List<OnlineProductEx> subOnlineProducts) {
        List<SubSku> subSkus = masterOnlineProduct.getSubSkus();
        List<Variant> variants = masterOnlineProduct.getVariants();
        if (subSkus == null || subSkus.size() <= 0 || variants == null || variants.size() <= 0) {
            return;
        }
        // init value
        List<Variant> filteredVariants = new ArrayList<>();
        // variant_skus entity
        Map<String, String> variantSkus = new HashMap<>();
        // variant_count_map entity
        Map<String, Integer> variantCountMap = new HashMap<>();
        // resolve the valid attrs
        List<SubSku> subSkusFilterStatus = subSkus.stream().filter(e -> Objects.nonNull(e) && (SkuStatus.ACTIVE.getCode().equals(e.getStatus())
                || SkuStatus.SOLD_OUT.getCode().equals(e.getStatus()) || SkuStatus.AWAITING_TAX_CODE_ASSIGNMENT.getCode().equals(e.getStatus()))).collect(Collectors.toList());
        Set<String> dropSku = subSkusFilterStatus.stream().map(SubSku::getSimilarProducts).filter(Objects::nonNull)
                .map(SimilarProducts::getRelatedSkus).filter(Objects::nonNull).collect(Collectors.toSet());
        subSkusFilterStatus.stream().filter(subSku -> !dropSku.contains(subSku.getSkuNumber())).forEach(subSku -> {
            // status in 1 or 4 or 12
            variantSkus.put(subSku.getAttrs(), subSku.getSkuNumber());
            //0: 10,1: None
            String attrs = subSku.getAttrs();
            if (StringUtils.isEmpty(attrs)) {
                return;
            }
            Arrays.stream(attrs.split(",")).filter(e -> e != null && e.contains(":")).forEach(str -> {
                int indexOfSplit = str.indexOf(":");
                String variantId = str.substring(0, indexOfSplit).trim();
                String variantContent = str.substring(indexOfSplit + 1).trim();
                if (filteredVariants.stream().map(Variant::getVariantId).noneMatch(variantId::equals)) {
                    variants.stream().filter(variant -> variant.getVariantId().equals(variantId)).findFirst().ifPresent(old -> {
                        Variant temp = new Variant();
                        temp.setVariantId(variantId);
                        temp.setVariantUOM(old.getVariantUOM());
                        temp.setVariantType(old.getVariantType());
                        temp.setVariantName(old.getVariantName());
                        temp.setVariantContent(new ArrayList<>());
                        temp.setVariantSwatchUrls(new HashMap<>());
                        filteredVariants.add(temp);
                    });
                }
                // handler the resolved variant content
                if (StringUtils.isEmpty(variantContent)) {
                    return;
                }
                Map<String, String> variantSwatchUrls = variants.stream().filter(variant -> "Color".equals(variant.getVariantName())).findFirst().map(Variant::getVariantSwatchUrls).orElse(Collections.emptyMap());
                filteredVariants.stream().filter(variant -> variant.getVariantId().equals(variantId)).forEach(currentVariant -> {
                    List<String> variantContents = currentVariant.getVariantContent();
                    if (variantContents.contains(variantContent)) {
                        return;
                    }
                    variantContents.add(variantContent);
                    // fill the watch url of color variant
                    if (!"Color".equalsIgnoreCase(currentVariant.getVariantName())) {
                        return;
                    }
                    // generate swatch url
                    subOnlineProducts.stream().filter(e -> e.getSkuNumber().equals(subSku.getSkuNumber())).forEach(subOnlineProduct -> {
                        // medias null case
                        Media media = Optional.ofNullable(subOnlineProduct.getMedia())
                                .flatMap(mediaList -> mediaList.stream().filter(e -> Objects.nonNull(e.getSort())).min(Comparator.comparing(Media::getSort))).orElse(new Media());
                        currentVariant.getVariantSwatchUrls().put(variantContent, supplementUrl(media, variantContent, variantSwatchUrls));
                    });
                });
            });
        });
        // fix filterVariant
        List<String> sizes = new ArrayList<>();
        List<String> sizeUoms = new ArrayList<>();
        Set<String> sizeSet = new HashSet<>();
        AtomicBoolean flag = new AtomicBoolean(false);
        // FIX MIK-10151: should filter deleted products and EA products
        subOnlineProducts.stream()
                .filter(e -> Objects.nonNull(e) && (SkuStatus.ACTIVE.getCode().equals(e.getStatus())
                        || SkuStatus.SOLD_OUT.getCode().equals(e.getStatus()) || SkuStatus.AWAITING_TAX_CODE_ASSIGNMENT.getCode().equals(e.getStatus())))
                .filter(e -> !ChannelType.ASSORTMENT.getCode().equals(e.getChannel()))
                .map(OnlineProductEx::getItemProperties).filter(itemProperties -> itemProperties.getSize() != null && !itemProperties.getSize().isBlank())
                .filter(itemProperties -> sizeSet.add(itemProperties.getSize())).sorted(SizeComparator.getInstance()).forEach(itemProperties -> {
                    sizes.add(itemProperties.getSize());
                    sizeUoms.add(itemProperties.getSizeUom() == null ? "" : itemProperties.getSizeUom());
                    flag.set(true);
                });
        if(flag.get()) {
            filteredVariants.stream().filter(variant -> "Size".equalsIgnoreCase(variant.getVariantName())).forEach(variant -> {
                variant.setVariantUOM(sizeUoms);
                variant.setVariantContent(sizes);
            });
        }
        Optional.of(filteredVariants).filter(e -> e.size() > 0).ifPresent(list -> list.forEach(newFilteredVariant -> variantCountMap.put(newFilteredVariant.getVariantName(), newFilteredVariant.getVariantContent().size())));
        Stream.of(subOnlineProducts, List.of(masterOnlineProduct)).flatMap(List::stream).forEach(productEx -> {
            productEx.setFilteredVariants(filteredVariants);
            productEx.setVariantCountMap(variantCountMap);
            productEx.setVariantSkus(variantSkus);
            productEx.setSubSkuWithColor(resolveSubSkuWithColor(productEx.getSkuNumber(), variants, subOnlineProducts));
        });
    }

    public static List<SubSkuWithColor> resolveSubSkuWithColor(String skuNumber, List<Variant> variants, List<OnlineProductEx> subSkuList) {
        // sub_skus_with_color
        List<SubSkuWithColor> subSkuWithColors = new ArrayList<>();
        OnlineProductEx onlineProductEx = subSkuList.stream().filter(item -> !Objects.equals(item.getSkuType(), SkuType.MASTER.getCode()) && item.getSkuNumber().equals(skuNumber)).findFirst().orElse(null);
        Variant colorVariant = variants.stream().filter(variant -> "Color".equalsIgnoreCase(variant.getVariantName())).findFirst().orElse(null);
        if (colorVariant == null) {
            return subSkuWithColors;
        }
        int supplement = 3;
        List<String> remainingContents = colorVariant.getVariantContent();
        if (onlineProductEx != null) {
            SubSkuWithColor subSkuWithColor = new SubSkuWithColor();
            subSkuWithColor.setSkuNumber(onlineProductEx.getSkuNumber());
            String color = onlineProductEx.getItemProperties().getColorName();
            Media media = onlineProductEx.getMedia().stream().filter(e -> Objects.nonNull(e.getSort())).min(Comparator.comparing(Media::getSort)).orElse(new Media());
            subSkuWithColor.setSwatchImageUrl(supplementUrl(media, color, colorVariant.getVariantSwatchUrls()));
            subSkuWithColor.setColorName(color);
            subSkuWithColor.setThumbnailUrl(onlineProductEx.getThumbnailUrl());
            if (onlineProductEx.getMedia() != null && onlineProductEx.getMedia().size() > 0) {
                subSkuWithColor.setFullSizeUrl(onlineProductEx.getMedia().get(0).getFullSizeUrl());
                subSkuWithColor.setThumbnailUrl(onlineProductEx.getMedia().get(0).getThumbnailUrl());
            }
            subSkuWithColor.setShipping(onlineProductEx.getShipping());
            subSkuWithColors.add(subSkuWithColor);
            supplement--;
            remainingContents = colorVariant.getVariantContent().stream().filter(e -> !e.equals(color)).collect(Collectors.toList());
        }
        for (String color : remainingContents) {
            SubSkuWithColor temp = new SubSkuWithColor();
            OnlineProductEx colorRelatedSku = subSkuList.stream().filter(item -> color.equals(item.getItemProperties().getColorName()) &&
                            (SkuStatus.ACTIVE.getCode().equals(item.getStatus()) || SkuStatus.SOLD_OUT.getCode().equals(item.getStatus()) || SkuStatus.AWAITING_TAX_CODE_ASSIGNMENT.getCode().equals(item.getStatus())))
                    .findFirst().orElse(null);
            if (colorRelatedSku == null) {
                continue;
            }
            temp.setSkuNumber(colorRelatedSku.getSkuNumber());
            Media media = colorRelatedSku.getMedia().stream().filter(e -> Objects.nonNull(e.getSort())).min(Comparator.comparing(Media::getSort)).orElse(new Media());
            temp.setSwatchImageUrl(supplementUrl(media, color, colorVariant.getVariantSwatchUrls()));
            temp.setColorName(color);
            temp.setThumbnailUrl(colorRelatedSku.getThumbnailUrl());
            if (colorRelatedSku.getMedia() != null && colorRelatedSku.getMedia().size() > 0) {
                temp.setFullSizeUrl(colorRelatedSku.getMedia().get(0).getFullSizeUrl());
                temp.setThumbnailUrl(colorRelatedSku.getMedia().get(0).getThumbnailUrl());
            }
            temp.setShipping(colorRelatedSku.getShipping());
            subSkuWithColors.add(temp);
            if (--supplement < 0) {
                break;
            }
        }
        return subSkuWithColors;
    }

    public static String supplementUrl(Media media, String color, Map<String, String> variantSwatchUrls) {
        String supplementedUrl = doSupplementUrl(media, color, variantSwatchUrls);
        if (supplementedUrl != null && (supplementedUrl.contains("imgs.com") || supplementedUrl.contains("static.platform.com"))) {
            supplementedUrl = (supplementedUrl.contains("?") ? supplementedUrl.substring(0, supplementedUrl.indexOf("?")) : supplementedUrl) + "?fit=inside|60:60";
        }
        return supplementedUrl;
    }

    public static String doSupplementUrl(Media media, String color, Map<String, String> variantSwatchUrls) {
        // swatch_url,color_image_url,variant_swatch_url,small_image_url,large_url,fullsize_url
        if (!StringUtils.isEmpty(media.getSwatchImageUrl())) {
            return media.getSwatchImageUrl();
        }
        if (!StringUtils.isEmpty(media.getColorImageUrl())) {
            return media.getColorImageUrl();
        }
        if (color != null && variantSwatchUrls != null && variantSwatchUrls.containsKey(color)) {
            return variantSwatchUrls.get(color);
        }
        if (!StringUtils.isEmpty(media.getSmallImageUrl())) {
            return media.getSmallImageUrl();
        }
        if (!StringUtils.isEmpty(media.getLargeUrl())) {
            return media.getLargeUrl();
        }
        if (!StringUtils.isEmpty(media.getFullSizeUrl())) {
            return media.getFullSizeUrl();
        }
        return null;
    }

}