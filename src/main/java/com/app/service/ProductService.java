package com.app.service;

import com.app.dto.ProductDto;
import com.app.exceptions.AppException;
import com.app.exceptions.ProductException;
import com.app.mappers.ModelMapper;
import com.app.model.Product;
import com.app.repository.ProductRepository;
import com.app.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public ProductDto addProduct(ProductDto productDto) {
		ProductValidator productValidator = new ProductValidator();
		Map<String, String> errors = productValidator.validate(productDto);
		if (productValidator.hasErrors()) {
			String errorMessage = errors
					.entrySet()
					.stream()
					.map(e -> e.getKey() + ": " + e.getValue())
					.collect(Collectors.joining(", "));
			throw new ProductException(errorMessage);
		}

		Product product = ModelMapper.fromProductDtoToProduct(productDto);
		return ModelMapper.fromProductToProductDto(productRepository.save(product));
	}

	public ProductDto updateProduct(Long id, ProductDto productDto) {
		if (id == null) {
			throw new AppException("Product id is null");
		}
		if (productDto == null) {
			throw new AppException("Product is null");
		}

		Product product = productRepository
				.findById(id)
				.orElseThrow();

		product.setPrice(product.getPrice() == null ?
				product.getPrice() : productDto.getPrice());

		product.setCategory(product.getCategory() == null ?
				product.getCategory() : productDto.getCategory());

		Product updatedProduct = productRepository.save(product);
		return ModelMapper.fromProductToProductDto(updatedProduct);

	}

	public List<ProductDto> getProducts() {
		return productRepository.findAll()
				.stream()
				.map(ModelMapper::fromProductToProductDto)
				.collect(Collectors.toList());
	}

	public ProductDto getOneProduct(Long id) {
		if (id == null) {
			throw new ProductException("Product id is null");
		}
		return productRepository.findById(id)
				.map(ModelMapper::fromProductToProductDto)
				.orElseThrow();
	}

	public ProductDto deleteOneProduct(Long id) {
		if (id == null) {
			throw new ProductException("Product id is null");
		}
		Product product = productRepository.findById(id)
				.orElseThrow();
		productRepository.deleteById(id);
		return ModelMapper.fromProductToProductDto(product);
	}
}
