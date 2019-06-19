package com.codebydhiren.coffeeshopitemmanagement;

import com.codebydhiren.coffeeshopitemmanagement.model.Product;
import com.codebydhiren.coffeeshopitemmanagement.repository.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@SpringBootApplication
public class CoffeeShopItemManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeShopItemManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ProductRepository repository) {
		return args -> {
			repository.deleteAll();

			Flux<Product> productFlux = Flux.just(new Product(null, "Iphone XS", BigDecimal.valueOf(550.78)),
					new Product(null, "Iphone XR", BigDecimal.valueOf(760.78)),
					new Product(null, "Iphone X", BigDecimal.valueOf(620.78)),
					new Product(null, "Iphone 8", BigDecimal.valueOf(520.78)),
					new Product(null, "Google Pixel 2", BigDecimal.valueOf(320.78)),
					new Product(null, "Google Pixel 2xl", BigDecimal.valueOf(540.78)),
					new Product(null, "Google Pixel 3", BigDecimal.valueOf(626.78)),
					new Product(null, "Google Pixel 3xl", BigDecimal.valueOf(820.78)),
					new Product(null, "OnePlus 6t", BigDecimal.valueOf(430.78)),
					new Product(null, "OnePlus 7", BigDecimal.valueOf(458.78)),
					new Product(null, "OnePlus 7pro", BigDecimal.valueOf(789.78))
			).flatMap(repository :: save);

			productFlux.thenMany(repository.findAll())
					.subscribe(System.err::println);

		};
	}

	//@Bean
	public ApplicationRunner initOnceAgain(ReactiveMongoOperations operations, ProductRepository repository) {
		return args -> {

			Flux<Product> productFlux = Flux.just(new Product(null, "Iphone XS", BigDecimal.valueOf(550.78)),
					new Product(null, "Iphone XR", BigDecimal.valueOf(760.78)),
					new Product(null, "Iphone X", BigDecimal.valueOf(620.78)),
					new Product(null, "Iphone 8", BigDecimal.valueOf(520.78))
			).flatMap(repository :: save);

			operations.collectionExists(Product.class)
					.flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists))
					.thenMany(v -> operations.createCollection(Product.class))
					.thenMany(productFlux)
					.thenMany(repository.findAll())
					.subscribe(System.out::println);
		};
	}

}
