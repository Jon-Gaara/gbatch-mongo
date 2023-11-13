package com.example.gbatchmongo.service.impl;

import com.example.gbatchmongo.common.handle.IJobHandler;
import com.example.gbatchmongo.configuration.BatchJobExecutor;
import com.example.gbatchmongo.model.ReturnT;
import com.example.gbatchmongo.model.TriggerParam;
import com.example.gbatchmongo.service.ExecutorBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;


public class ExecutorBizImpl implements ExecutorBiz {

    private final static Logger logger = LoggerFactory.getLogger(ExecutorBizImpl.class);
    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        IJobHandler jobHandler = BatchJobExecutor.loadJobHandler(triggerParam.getExecutorHandler());
        ReturnT<String> executeResult = null;
        if(jobHandler != null){
            try {
                executeResult = jobHandler.execute(triggerParam.getExecutorParams());
            } catch (Throwable e) {
                if(e instanceof InvocationTargetException){
                    e = ((InvocationTargetException) e).getTargetException();
                }
                String errorMsg = toString(e);
                final String handle = triggerParam.getExecutorHandler();
                logger.error("JobThread run failed,handle:{},msg:{}",handle,errorMsg, e);
                executeResult = new ReturnT<>(ReturnT.FAIL_CODE, errorMsg);
            }
        }
        return executeResult;
    }

    private String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.append(e.getClass().getName()).append(":").append(e.getMessage());
        return stringWriter.toString();
    }
}
