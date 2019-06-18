package com.codebydhiren.coffeeshopitemmanagement.router;

import com.codebydhiren.coffeeshopitemmanagement.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(ProductHandler productHandler) {
        return RouterFunctions.route(GET("/products/functional").and(accept(MediaType.APPLICATION_JSON)) , productHandler :: getAllProducts);
    }

}
