
package com.codebydhiren.coffeeshopitemmanagement.handler;

import com.codebydhiren.coffeeshopitemmanagement.model.Product;
import com.codebydhiren.coffeeshopitemmanagement.model.ProductEvent;
import com.codebydhiren.coffeeshopitemmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ProductHandler {

	@Autowired
	private ProductRepository productRepository;

	public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {

		final Flux<Product> allProducts = productRepository.findAll();
		return ServerResponse.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(allProducts, Product.class);
	}

	public Mono<ServerResponse> getSpecificProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<ServerResponse> notFound = ServerResponse.notFound()
		        .build();

		return productRepository.findById(id)
		        .flatMap(item -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .body(fromObject(item)))
		        .switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> addProduct(ServerRequest serverRequest) {

		final Mono<Product> productMono = serverRequest.bodyToMono(Product.class);

		return productMono.flatMap(product -> ServerResponse.status(HttpStatus.CREATED)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(productRepository.save(product), Product.class));
	}

	public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {

		// extracting the path variable from the request url
		String productId = serverRequest.pathVariable("id");

		// new product to be updated
		Mono<Product> productMonoToBeUpdated = serverRequest.bodyToMono(Product.class);

		// product found by id
		Mono<Product> existingProductById = productRepository.findById(productId);

		// response to be provided if nothing found in the requested id
		Mono<ServerResponse> notFound = ServerResponse.notFound()
		        .build();

		return productMonoToBeUpdated
		        .zipWith(existingProductById,
		                 (productMono,
		                  existingProduct) -> new Product(existingProduct.getProductId(),
		                                                  productMono.getProductName(),
		                                                  productMono.getPrice()))
		        .flatMap(product -> ServerResponse.ok()
		                .contentType(MediaType.APPLICATION_JSON)
		                .body(productRepository.save(product), Product.class))
		        .switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {

		// extracting the path variable from the request url
		String productId = serverRequest.pathVariable("id");

		// response to be provided if nothing found in the requested id
		Mono<ServerResponse> notFound = ServerResponse.notFound()
		        .build();

		Mono<Product> productMono = productRepository.findById(productId);

		return productMono.flatMap(product -> ServerResponse.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .build(productRepository.delete(product)))
		        .switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> deleteAllProduct(ServerRequest serverRequest) {
		return ServerResponse.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .build(productRepository.deleteAll());
	}

	public Mono<ServerResponse> productEvents(ServerRequest serverRequest) {

		Flux<ProductEvent> productEventFlux = Flux.interval(Duration.ofSeconds(1))
		        .map(item -> {
			        return new ProductEvent(UUID.randomUUID()
			                .toString(), Instant.now(), item);
		        });

		return ServerResponse.ok()
		        .contentType(MediaType.TEXT_EVENT_STREAM)
		        .body(productEventFlux, ProductEvent.class);
	}


}
