package com.linktic.products_service;

import org.springframework.boot.SpringApplication;

public class TestProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
