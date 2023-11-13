package com.example.gbatchmongo.service;

import com.example.gbatchmongo.model.ReturnT;
import com.example.gbatchmongo.model.TriggerParam;

public interface ExecutorBiz {

    ReturnT<String> run(TriggerParam triggerParam);
}
