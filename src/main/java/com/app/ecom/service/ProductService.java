package com.app.ecom.service;

import com.app.ecom.dto.ProductRequestDTO;
import com.app.ecom.dto.ProductResponseDTO;
import com.app.ecom.entities.Product;
import com.app.ecom.mapper.ProductMapper;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    public @Nullable ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        return productRepository.findById(id).map(existingProduct -> {
            productMapper.updateEntityFromDTO(productRequestDTO, existingProduct);
            Product updatedProduct = productRepository.save(existingProduct);
            return productMapper.toDTO(updatedProduct);
        });
    }

    public @Nullable List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAByActiveTrue();
        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }).orElse(false);
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(productMapper::toDTO)
                .toList();
    }
}
