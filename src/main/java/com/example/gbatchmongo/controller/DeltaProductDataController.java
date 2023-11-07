package com.example.gbatchmongo.controller;

import com.example.gbatchmongo.model.ReturnT;
import com.example.gbatchmongo.service.ProductDataJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DeltaProductDataController {

    @Autowired
    private ProductDataJobService productDataJobService;

    @GetMapping("/uploadDeltaProductDataFileToGCS")
    public ReturnT<String> uploadDeltaProductDataFileToGCS(){
        String timeFolder = "first_time";
        String[] fields = {"item_price", "filtered_variants"};
        try {
            productDataJobService.uploadDeltaProductDataFileToGCS("dev",timeFolder,fields);
        }catch (Exception e){
            return new ReturnT<>(ReturnT.FAIL_CODE,e.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
