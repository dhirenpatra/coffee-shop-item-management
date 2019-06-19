# coffee-shop-item-management
A sample project on spring webflux

WebClient webClient = WebClient.create();

WebClient webClient = WebClient.create("http://www.googleapis.com");

WebClient webClient = WebClient.builder()
                                .baseUrl("http://www.googleapis.com")
                                .defaultHeader(HttpHeader.USERAGENT, "SpringBoot")
                                .build();

===================================================================================

WebClient webClient = WebClient.mutate()
                                .baseUrl("http://www.googleapis.com")
                                .defaultHeader(HttpHeader.USERAGENT, "SpringBoot")
                                .build();

===================================================================================

webClient.get()    // WebClient.RequestHeadersUriSpec
            .uri("/products/{id}" , id)     // WebClient.UriSpec
            .accept(MediaType.APPLICATION_JSON)     // WebClient.RequestHeadersSpec

===================================================================================

webClient.post()    // WebClient.RequestBodyUriSpec
            .uri("/products/")     // WebClient.UriSpec
            .accept(MediaType.APPLICATION_JSON)     // WebClient.RequestHeadersSpec

===================================================================================

Sending Form Data

webClient.post()
            .uri("/products")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData("field1" ,"value1").with("field2" ,"value2"))
            .body(BodyInserters.fromMultipartData("field1" ,"value1").with("field2" ,"value2"))

===================================================================================

retrieve() and exchange() method

retrieve() ----> bodyToFlux(Y.class) ------> bodyToMono(X.class)

We can also have a function to be executed when 4XXServerError happens

webClient.get()
            .uri("/products/{id}" , id)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is4xxServerError, response -> .......)
            .bodyToFlux(Z.class);

exchange() -----> exchange().flatMap(res -> res.bodyToFlux(x.class));
                                // res.toEntityList(x.class) // res.toEntity(x.class)

===================================================================================