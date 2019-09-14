
package com.codebydhiren.coffeeshopitemmanagement.router;

import com.codebydhiren.coffeeshopitemmanagement.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

	/**
	 * Order of Routes are impo
	 *
	 * @param productHandler
	 * @return
	 */
	@Bean
	public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
		return RouterFunctions
		        .route(GET("/products/functional").and(accept(MediaType.APPLICATION_JSON)),
		               productHandler::getAllProducts)
		        .andRoute(POST("/products/functional").and(accept(MediaType.APPLICATION_JSON)),
		                  productHandler::addProduct)
		        .andRoute(DELETE("/products/functional").and(accept(MediaType.APPLICATION_JSON)),
		                  productHandler::deleteAllProduct)
		        .andRoute(GET("/products/functional/event")
		                .and(accept(MediaType.TEXT_EVENT_STREAM)), productHandler::productEvents)
		        .andRoute(GET("/products/functional/{id}").and(accept(MediaType.APPLICATION_JSON)),
		                  productHandler::getSpecificProduct)
		        .andRoute(DELETE("/products/functional/{id}")
		                .and(accept(MediaType.APPLICATION_JSON)), productHandler::deleteProduct)
		        .andRoute(PUT("/products/functional/{id}").and(accept(MediaType.APPLICATION_JSON)),
		                  productHandler::updateProduct);
	}

}
