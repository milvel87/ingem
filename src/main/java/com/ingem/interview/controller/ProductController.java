package com.ingem.interview.controller;

import com.ingem.interview.exception.CustomRequestErrorException;
import com.ingem.interview.model.Product;
import com.ingem.interview.repository.ProductRepository;
import com.ingem.interview.util.CurrencyUtility;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;
    private final CurrencyUtility currencyUtility;

    public ProductController(ProductRepository productRepository, CurrencyUtility currencyUtility) {
        this.productRepository = productRepository;
        this.currencyUtility = currencyUtility;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() throws Exception {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "id") Long id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomRequestErrorException( "id", HttpStatus.NOT_FOUND, "id", "Product not found"));
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(Product.class.getConstructor(), -1), result);
        }
        if (product.getId() == null) {
            Optional<Product> existingProduct = productRepository.findByCode(product.getCode());
            if (existingProduct.isPresent()) {
                throw new CustomRequestErrorException("code", HttpStatus.BAD_REQUEST, "code", "Duplicate code");
            }
            product.setPriceEur(currencyUtility.hrkToEur(product.getPriceHrk()));
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } else {
            throw new CustomRequestErrorException( "id", HttpStatus.BAD_REQUEST, "id", "Must be null");
        }
    }

    @PutMapping()
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(Product.class.getConstructor(), -1), result);
        }
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new CustomRequestErrorException( "id", HttpStatus.NOT_FOUND, "id", "Product not found"));
        if (!product.getCode().equals(existingProduct.getCode())) {
            Optional<Product> productWithSameCode = productRepository.findByCode(product.getCode());
            if (productWithSameCode.isPresent()) {
                throw new CustomRequestErrorException( "code", HttpStatus.BAD_REQUEST, "code", "Duplicate code");
            }
        }
        if (product.getPriceHrk() != existingProduct.getPriceHrk()) {
            product.setPriceEur(currencyUtility.hrkToEur(product.getPriceHrk()));
        }
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable(value = "id") Long id) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new CustomRequestErrorException( "id", HttpStatus.NOT_FOUND, "id", "Product not found"));
        productRepository.delete(existingProduct);
        return ResponseEntity.noContent().build();
    }

}