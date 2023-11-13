package com.example.gbatchmongo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.gbatchmongo.common.annotation.BatchJob;
import com.example.gbatchmongo.common.enums.ChannelType;
import com.example.gbatchmongo.model.ReturnT;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UpdateVariantsJob {

    private final int queueSize = 10_000;
    private final OnlineProductVariantService onlineProductVariantService;

    public UpdateVariantsJob(OnlineProductVariantService onlineProductVariantService) {
        this.onlineProductVariantService = onlineProductVariantService;
    }

    @BatchJob("updateSubSkusWithColor")
    public ReturnT updateSubSkusWithColor(String params) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        log.info("Update variant major job start ...");

        try {
            Optional<JSONObject> jsonObjectOptional = Optional.ofNullable(params).filter(JSONObject::isValidObject).map(JSONObject::parseObject);
            Integer batch = jsonObjectOptional.map(jsonObject -> jsonObject.getInteger("batch")).orElse(500);
            List<Integer> channels = jsonObjectOptional.map(jsonObject -> jsonObject.getJSONArray("channel")).map(array -> array.toJavaList(Integer.class))
                    .orElse(Arrays.stream(ChannelType.values()).filter(channelType -> !ChannelType.UNKNOWN.equals(channelType)).map(ChannelType::getCode).collect(Collectors.toList()));
            log.info("Update variant major job, params: {}, batch: {}, channels: {}", params, batch, channels);
            onlineProductVariantService.unsetVariant();
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(queueSize), new DefaultThreadFactory("Pool-online-product-variant-service"), new ThreadPoolExecutor.CallerRunsPolicy());
            onlineProductVariantService.updateVariantMajor(batch, channels, threadPoolExecutor);
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(100, TimeUnit.MINUTES);
        } catch (Exception exception) {
            log.error("Update variant major job  fail", exception);
            throw exception;
        }
        log.info("Update variant major job time pass " + (System.currentTimeMillis() - startTime));
        return new ReturnT<String>(ReturnT.SUCCESS_CODE, "Update variant major job successfully");
    }

    @BatchJob("updateAttr")
    public ReturnT updateAttr(String params) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        log.info("Update attr job start ...");

        try {
            Optional<JSONObject> jsonObjectOptional = Optional.ofNullable(params).filter(JSONObject::isValidObject).map(JSONObject::parseObject);
            Integer batch = jsonObjectOptional.map(jsonObject -> jsonObject.getInteger("batch")).orElse(500);
            List<Integer> channels = jsonObjectOptional.map(jsonObject -> jsonObject.getJSONArray("channel")).map(array -> array.toJavaList(Integer.class))
                    .orElse(Arrays.stream(ChannelType.values()).filter(channelType -> !ChannelType.UNKNOWN.equals(channelType)).map(ChannelType::getCode).collect(Collectors.toList()));
            log.info("Update variant attr job, params: {}, batch: {}, channels: {}", params, batch, channels);
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(queueSize), new DefaultThreadFactory("Pool-online-product-variant-service"), new ThreadPoolExecutor.CallerRunsPolicy());
            onlineProductVariantService.updateAttr(batch, channels, threadPoolExecutor);
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(100, TimeUnit.MINUTES);
        } catch (Exception exception) {
            log.error("Update attr job fail", exception);
            throw exception;
        }
        log.info("Update attr job time pass " + (System.currentTimeMillis() - startTime));
        return new ReturnT<String>(ReturnT.SUCCESS_CODE, "Update attr job successfully");
    }
}
