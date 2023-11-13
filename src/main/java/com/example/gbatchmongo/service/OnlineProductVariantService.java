package com.example.gbatchmongo.service;


import com.example.gbatchmongo.common.enums.SkuType;
import com.example.gbatchmongo.helper.OnlineProductHelper;
import com.example.gbatchmongo.model.OnlineProductEx;
import com.example.gbatchmongo.model.SubSku;
import com.google.common.collect.Iterators;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

@Service
public class OnlineProductVariantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineProductVariantService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateAttr(Integer batch, List<Integer> channels, ThreadPoolExecutor poolExecutor) {
        Query query = new Query().addCriteria(Criteria.where("sku_type").is(SkuType.MASTER.getCode()).and("sub_skus").exists(true).and("channel").in(channels));
        query.fields().include("_id").include("sku_type").include("sku_number").include("variants").include("sub_skus");
        CloseableIterator<OnlineProductEx> stream = mongoTemplate.stream(query, OnlineProductEx.class);
        Iterators.partition(stream, batch).forEachRemaining(onlineProducts -> poolExecutor.execute(new AttrRunnable(onlineProducts)));
    }

    public void updateVariantMajor(Integer batch, List<Integer> channels, ThreadPoolExecutor poolExecutor) {
        Query query = new Query().addCriteria(Criteria.where("sku_type").is(SkuType.MASTER.getCode()).and("sub_skus").exists(true).and("channel").in(channels));
        query.fields().include("_id").include("sku_type").include("sku_number").include("variants").include("sub_skus");
        CloseableIterator<OnlineProductEx> stream = mongoTemplate.stream(query, OnlineProductEx.class);
        Iterators.partition(stream, batch).forEachRemaining(onlineProducts -> poolExecutor.execute(new MajorRunnable(onlineProducts)));
    }

    class AttrRunnable implements Runnable {
        private final List<OnlineProductEx> onlineProductExList;

        public AttrRunnable(List<OnlineProductEx> onlineProductExList) {
            this.onlineProductExList = onlineProductExList;
        }

        @Override
        public void run() {
            try {
                List<Pair<Query, Update>> updates = new ArrayList<>();
                LocalDateTime now = LocalDateTime.now();
                for (OnlineProductEx onlineProductEx : onlineProductExList) {
                    List<SubSku> subSkus = onlineProductEx.getSubSkus();
                    if (!CollectionUtils.isEmpty(subSkus)) {
                        for (SubSku subSku : subSkus) {
                            if (StringUtils.hasLength(subSku.getAttrs())) {
                                Query updateQuery = new Query().addCriteria(new Criteria("_id").is(subSku.getSkuNumber()));
                                Update update = new Update().set("variant_attr", subSku.getAttrs()).set("update_time", now);
                                updates.add(Pair.of(updateQuery, update));
                            }
                        }
                    }
                }

                if (updates.size() > 0) {
                    mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, OnlineProductEx.class).updateMulti(updates).execute();
                }
            } catch (Exception e) {
                LOGGER.error("AttrRunnable Exception.", e);
            }
        }
    }

    public void unsetVariant() {
        Criteria criteria = Criteria.where("sku_type").ne(SkuType.MASTER.getCode()).and("master_sku_number").exists(false);
        criteria.orOperator(
                Criteria.where("variant_skus").exists(true),
                Criteria.where("filtered_variants").exists(true),
                Criteria.where("variant_count_map").exists(true),
                Criteria.where("sub_skus_with_color").exists(true),
                Criteria.where("item_price.price_range").exists(true),
                Criteria.where("item_price.org_price_range").exists(true),
                Criteria.where("item_price.unit_price_range").exists(true)
        );
        Query query = new Query(criteria);
        query.fields().include("_id");
        try (CloseableIterator<OnlineProductEx> stream = mongoTemplate.stream(query, OnlineProductEx.class)) {
            stream.forEachRemaining(onlineProductEx -> {
                Update update = new Update();
                update.unset("variant_skus")
                        .unset("filtered_variants")
                        .unset("variant_count_map")
                        .unset("sub_skus_with_color")
                        .unset("item_price.price_range")
                        .unset("item_price.org_price_range")
                        .unset("item_price.unit_price_range");
                mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(onlineProductEx.getProductId())), update, OnlineProductEx.class);
            });
        }
    }

    class MajorRunnable implements Runnable {
        private final List<OnlineProductEx> onlineProducts;

        public MajorRunnable(List<OnlineProductEx> onlineProducts) {
            this.onlineProducts = onlineProducts;
        }

        @Override
        public void run() {
            try {
                List<Pair<Query, Update>> updates = new ArrayList<>();
                onlineProducts.forEach(onlineProduct -> {
                    try {
                        if (onlineProduct == null || CollectionUtils.isEmpty(onlineProduct.getSubSkus()) || CollectionUtils.isEmpty(onlineProduct.getVariants())) {
                            return;
                        }
                        Query subSkuQuery = Query.query(Criteria.where("master_sku_number").is(onlineProduct.getSkuNumber()).and("sku_number").ne(onlineProduct.getSkuNumber()));
                        subSkuQuery.fields().include("_id", "sku_number", "media", "status", "channel", "item_properties", "sku_type");
                        List<OnlineProductEx> subProducts = mongoTemplate.find(subSkuQuery, OnlineProductEx.class);
                        OnlineProductHelper.fillSubSkuWithColorAndVariant(onlineProduct, subProducts);

                        LocalDateTime now = LocalDateTime.now();
                        Stream.concat(subProducts.stream(), Stream.of(onlineProduct)).forEach(product -> {
                            Update update = Update.update("update_time", now)
                                    .set("filtered_variants", product.getFilteredVariants())
                                    .set("variant_count_map", product.getVariantCountMap())
                                    .set("variant_skus", product.getVariantSkus())
                                    .set("sub_skus_with_color", product.getSubSkuWithColor());
                            updates.add(Pair.of(Query.query(Criteria.where("_id").is(product.getProductId())), update));
                        });
                    } catch (Exception e) {
                        LOGGER.error("get variants error, sku: {}", onlineProduct.getSkuNumber(), e);
                    }
                });
                if (updates.size() > 0) {
                    mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, OnlineProductEx.class).updateMulti(updates).execute();
                }
            } catch (Exception e) {
                LOGGER.error("MajorRunnable Exception.", e);
            }
        }
    }
}
