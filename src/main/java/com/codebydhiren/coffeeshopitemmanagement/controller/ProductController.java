package com.codebydhiren.coffeeshopitemmanagement.controller;

import com.codebydhiren.coffeeshopitemmanagement.model.Product;
import com.codebydhiren.coffeeshopitemmanagement.model.ProductEvent;
import com.codebydhiren.coffeeshopitemmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getIndividualProduct(@PathVariable String id) {
        return productRepository.findById(id)
                .map(item -> ResponseEntity.ok().body(item))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> addOneProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,
                                                       @RequestBody Product newProduct) {

        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setPrice(newProduct.getPrice());
                    existingProduct.setProductName(newProduct.getProductName());
                    return productRepository.save(existingProduct);
                })
                .map(item -> ResponseEntity.ok().body(item))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {

        return productRepository.findById(id)
                .flatMap( foundProduct ->
                        productRepository.delete(foundProduct)
                                .then(Mono.just(ResponseEntity.ok().<Void> build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping
    public Mono<Void> deleteAllProducts() {
        return productRepository.deleteAll();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> productEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(event -> new ProductEvent(UUID.randomUUID().toString(), Instant.now(), event));
    }

}