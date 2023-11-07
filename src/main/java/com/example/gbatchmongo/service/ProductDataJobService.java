package com.example.gbatchmongo.service;


import com.example.gbatchmongo.model.OnlineProductEx;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProductDataJobService {

    private final int threadCount = Math.max(1, Double.valueOf(Runtime.getRuntime().availableProcessors() * 0.25).intValue());

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    @Autowired
    private MongoTemplate mongoTemplate;

    public void uploadDeltaProductDataFileToGCS(String env,String timeFolder,String[] fields) {
        Query query = new Query();
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(1);
        Criteria criteria = new Criteria();
        criteria.and("channel").in(Lists.newArrayList("B2B", "COM"));
        criteria.and("update_time").gte(startDateTime.atZone(ZoneOffset.UTC).toLocalDateTime());
        query.addCriteria(criteria);
        query.fields().exclude(fields);
        query.with(Sort.by(Sort.Direction.ASC, "_id"));
        long count = mongoTemplate.count(query, OnlineProductEx.class);
        log.info("Incremental data {},total:{} ", endDateTime.toLocalDate(), count);
        CloseableIterator<OnlineProductEx> stream = mongoTemplate.stream(query, OnlineProductEx.class);
        String fileName = "";
        List<OnlineProductEx> productExList = new ArrayList<>();
        try {
            while (stream.hasNext()) {
                OnlineProductEx onlineProductEx = stream.next();
                productExList.add(onlineProductEx);
            }
            System.out.println(productExList);
        } catch (Exception e) {
            log.error("write Storage error3 fileName:{}", fileName, e);
            throw e;
        }
    }

}
