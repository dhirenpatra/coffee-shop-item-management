
package com.codebydhiren.coffeeshopitemmanagement.webclient;

import com.codebydhiren.coffeeshopitemmanagement.model.Product;
import com.codebydhiren.coffeeshopitemmanagement.model.ProductEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ProductWebClient {

	private WebClient webClient;

	ProductWebClient() {
		this.webClient = WebClient.builder()
		        .baseUrl("localhost:8080/products")
		        .build();
	}

	public static void main(String[] args) {
		ProductWebClient productWebClient = new ProductWebClient();

		productWebClient.addProduct()
		        .thenMany(productWebClient.getAllProducts())
		        .take(1)
		        .flatMap(p -> productWebClient
		                .updateProduct(p.getProductId(), p.getProductName() + "Updated",
		                               p.getPrice()
		                                       .add(BigDecimal.valueOf(29999))))
		        .thenMany(productWebClient.getAllProducts())
		        .subscribe(System.out::println);

	}

	public Mono<ResponseEntity<Product>> addProduct() {
		return webClient.post()
		        .body(Mono.just(new Product(null, "Nokia N16pro", BigDecimal.valueOf(760.78))),
		              Product.class)
		        .exchange()
		        .flatMap(clientResponse -> clientResponse.toEntity(Product.class))
		        .doOnSuccess(o -> System.out.println("POSTED : " + o));
	}

	public Flux<Product> getAllProducts() {
		return webClient.get()
		        .retrieve()
		        .bodyToFlux(Product.class)
		        .doOnNext(o -> System.out.println("ALL PRODUCTS : " + o));
	}

	public Mono<Product> getOneProduct(String id) {
		return webClient.get()
		        .uri("/{id}", id)
		        .retrieve()
		        .bodyToMono(Product.class)
		        .doOnSuccess(o -> System.out.println("PRODUCT IS : " + o));
	}

	public Mono<Product> updateProduct(String id, String name, BigDecimal price) {
		return webClient.put()
		        .uri("/{id}", id)
		        .body(Mono.just(new Product(null, name, price)), Product.class)
		        .retrieve()
		        .bodyToMono(Product.class)
		        .doOnSuccess(o -> System.out.println("UPDATED PRODUCT IS : " + o));
	}

	public Mono<Void> deleteProduct(String id) {
		return webClient.delete()
		        .uri("/{id}", id)
		        .retrieve()
		        .bodyToMono(Void.class)
		        .doOnSuccess(o -> System.out.println("DELETED PRODUCT IS : " + o));
	}

	public Flux<ProductEvent> getAllEvents() {
		return webClient.get()
		        .uri("/events")
		        .retrieve()
		        .bodyToFlux(ProductEvent.class);
	}

}
