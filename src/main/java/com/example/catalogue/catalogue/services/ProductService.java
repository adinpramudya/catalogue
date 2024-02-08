package com.example.catalogue.catalogue.services;

import com.example.catalogue.catalogue.entities.ProductEntity;
import com.example.catalogue.catalogue.exceptions.ClientException;
import com.example.catalogue.catalogue.exceptions.NotFoundException;
import com.example.catalogue.catalogue.globals.GlobalConstant;
import com.example.catalogue.catalogue.models.ProductModel;
import com.example.catalogue.catalogue.repository.ProductRepository;
import com.example.catalogue.catalogue.specs.ProductSpecs;
import com.example.catalogue.catalogue.validators.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements Serializable {
    private ProductRepository productRepository;
    private ProductValidator productValidator;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }


    public ProductEntity add(ProductModel productModel) throws ClientException {
        // validation
        productValidator.notNullCheckProductId(productModel.getId());
        productValidator.nullCheckName(productModel.getName());
        productValidator.validateName(productModel.getName());
        productValidator.nullCheckQuantity(productModel.getQuantity());
        productValidator.validateQuantity(productModel.getQuantity());
        productValidator.nullCheckCategoryId(productModel.getCategoryId());
        productValidator.validateCategoryId(productModel.getCategoryId());

        Long count = productRepository.countByName(productModel.getName());
        if (count > 0) {
            throw new ClientException("Product name is already existed");
        }

        ProductEntity product = new ProductEntity();
        product.setName(productModel.getName());
        product.setQuantity(productModel.getQuantity());
        product.setCategoryId(productModel.getCategoryId());
        product.setRecStatus(GlobalConstant.REC_STATUS_ACTIVE);
        product.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        product.setCreatorId(productModel.getActorId() == null ? 0 : productModel.getActorId());

        return productRepository.save(product);
    }

    public List<ProductEntity> findAll() {
        List<ProductEntity> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public List<ProductEntity> findAllByCriteria(ProductModel productModel) {
        List<ProductEntity> products = new ArrayList<>();
        ProductSpecs specs = new ProductSpecs(productModel);
        productRepository.findAll((Pageable) specs).forEach(products::add);

        return products;
    }

    public ProductEntity findById(Integer id) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(id);
        productValidator.validateProductId(id);

        ProductEntity product = productRepository.findById(id).orElse(null);
        productValidator.nullCheckObject(product);
        return product;
    }

    public ProductEntity edit(ProductModel productModel) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(productModel.getId());
        productValidator.validateProductId(productModel.getId());

        if (!productRepository.existsById(productModel.getId())) {
            throw new NotFoundException("Cannot find product with id: " + productModel.getId());
        }

        ProductEntity product = new ProductEntity();
        product = findById(productModel.getId());

        if (productModel.getName() != null) {
            productValidator.validateName(productModel.getName());

            Long count = productRepository.countByName(productModel.getName());
            if (count > 0) {
                throw new ClientException("Product name is already existed");
            }
            product.setName(productModel.getName());
        }

        if (productModel.getQuantity() != null) {
            productValidator.validateQuantity(productModel.getQuantity());
            product.setQuantity(productModel.getQuantity());

        }

        if (productModel.getCategoryId() != null) {
            productValidator.validateCategoryId(productModel.getCategoryId());
            product.setCategoryId(productModel.getCategoryId());
        }

        product.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        product.setUpdaterId(productModel.getActorId() == null ? 0 : productModel.getActorId());
        return productRepository.save(product);
    }

    public ProductEntity delete(ProductModel productModel) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(productModel.getId());
        productValidator.validateProductId(productModel.getId());

        if (!productRepository.existsById(productModel.getId())) {
            throw new NotFoundException("Cannot find product with id: " + productModel.getId());
        }

        ProductEntity product = new ProductEntity();
        product = findById(productModel.getId());

        if (product.getRecStatus().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("Product id (" + productModel.getId() + ") is already been deleted.");
        }

        product.setRecStatus(GlobalConstant.REC_STATUS_NON_ACTIVE);
        product.setDeletedDate(new Timestamp(System.currentTimeMillis()));
        product.setDeleterId(productModel.getActorId() == null ? 0 : productModel.getActorId());
        return productRepository.save(product);
    }


}
