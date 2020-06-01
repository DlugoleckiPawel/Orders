package com.app.controller;

import com.app.dto.ProductDto;
import com.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	@PostMapping
	public ProductDto addProduct(@RequestBody ProductDto productDto) {
		return productService.addProduct(productDto);
	}

	@PutMapping("/{id}")
	public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
		return productService.updateProduct(id, productDto);
	}

	@GetMapping
	public List<ProductDto> getProducts() {
		return productService.getProducts();
	}

	@GetMapping("/{id}")
	public ProductDto getOneProduct(@PathVariable Long id) {
		return productService.getOneProduct(id);
	}

	@DeleteMapping("/{id}")
	public ProductDto deleteOneProduct(@PathVariable Long id) {
		return productService.deleteOneProduct(id);
	}
}
