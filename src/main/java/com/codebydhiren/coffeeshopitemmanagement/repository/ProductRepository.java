package com.codebydhiren.coffeeshopitemmanagement.repository;

import com.codebydhiren.coffeeshopitemmanagement.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,String> {

}
