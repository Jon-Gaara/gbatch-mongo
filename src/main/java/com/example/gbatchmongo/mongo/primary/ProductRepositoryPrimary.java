package com.example.gbatchmongo.mongo.primary;

import com.example.gbatchmongo.model.OnlineProductEx;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPrimary extends MongoRepository<OnlineProductEx, String> {

  Optional<OnlineProductEx> findBySkuNumber(String sku_number);

  List<OnlineProductEx> findAllByMasterSkuNumberIn(String[] masterSkuNumber);

}
