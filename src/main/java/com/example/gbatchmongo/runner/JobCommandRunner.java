package com.example.gbatchmongo.runner;

import com.example.gbatchmongo.model.ReturnT;
import com.example.gbatchmongo.model.TriggerParam;
import com.example.gbatchmongo.service.ExecutorBiz;
import com.example.gbatchmongo.service.impl.ExecutorBizImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobCommandRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JobCommandRunner.class);

    @Value("${job.runner.enabled:true}")
    private String jobRunnerEnabled;

    private static final String BROADCAST_TOTAL = "JOB_BROADCAST_TOTAL";
    private static final String BROADCAST_INDEX = "JOB_BROADCAST_INDEX";
    private static final String BATCH_TASK_INDEX = "BATCH_TASK_INDEX";
    private static final String BATCH_TASK_COUNT = "BATCH_TASK_COUNT";

    @Override
    public void run(String... args) {
        if(!Boolean.TRUE.toString().equals(jobRunnerEnabled)){
            return;
        }
        final String[] javaArgs = getJavaArgs(args);
        if(javaArgs.length < 1){
            logger.warn("java args length too short");
            return;
        }
        final String batchTaskCountStr = getBatchTaskTotal();
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setExecutorHandler(javaArgs[0]);
        if(javaArgs.length > 1){
            triggerParam.setExecutorParams(javaArgs[1]);
        }
        if(StringUtils.isNumeric(batchTaskCountStr)){
            final int batchTaskCount = Integer.parseInt(batchTaskCountStr);
            if(batchTaskCount > 0){
                triggerParam.setBroadcastTotal(batchTaskCount);
                triggerParam.setBroadcastIndex(getBatchTaskIndex());
            }
        }

        // run job method
        ExecutorBiz executorBiz = new ExecutorBizImpl();
        ReturnT<String> returnT = executorBiz.run(triggerParam);

        //exit
        if(returnT != null && ReturnT.SUCCESS_CODE == returnT.getCode()){
            System.exit(0);
        }else {
            System.exit(1);
        }
    }

    private String[] getJavaArgs(String[] args){
        String[] javaArgs = args;
        if(javaArgs.length < 2){
            final String javaArgsStr = System.getenv("JAVA_ARGS");
            if(StringUtils.isNotBlank(javaArgsStr)){
                javaArgs = javaArgsStr.split(" ");
            }
        }
        return javaArgs;
    }

    private String getBatchTaskTotal(){
        String batchTaskTotalStr = System.getenv(BROADCAST_TOTAL);
        if(StringUtils.isBlank(batchTaskTotalStr)){
            batchTaskTotalStr = System.getenv(BATCH_TASK_COUNT);
        }
        return batchTaskTotalStr;
    }

    private int getBatchTaskIndex(){
        String batchTaskIndexStr = System.getenv(BROADCAST_INDEX);
        if(StringUtils.isBlank(batchTaskIndexStr)){
            batchTaskIndexStr = System.getenv(BATCH_TASK_INDEX);
        }
        return StringUtils.isNumeric(batchTaskIndexStr) ? Integer.parseInt(batchTaskIndexStr) : 0;
    }
}
