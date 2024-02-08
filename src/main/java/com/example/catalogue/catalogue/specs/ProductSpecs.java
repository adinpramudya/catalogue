package com.example.catalogue.catalogue.specs;

import com.example.catalogue.catalogue.entities.ProductEntity;
import com.example.catalogue.catalogue.globals.GlobalConstant;
import com.example.catalogue.catalogue.models.ProductModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs implements Specification<ProductEntity> {
    private  ProductModel productModel;
    public  ProductSpecs(ProductModel productModel){
        super();
        this.productModel = productModel;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb){
        Predicate p =cb.disjunction();

        if(productModel.getId() != null && productModel.getId() != 0){
            p.getExpressions().add(cb.equal(root.get("id"), productModel.getId()));
        }

        if(productModel.getName() != null && productModel.getName().length() > 0){
            p.getExpressions().add(cb.like(cb.lower(root.get("name")),"%"+productModel.getName().toLowerCase() + "%"));
        }

        if(productModel.getRecStatus() != null && (productModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_ACTIVE) || productModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE))){
            p.getExpressions().add(cb.equal(cb.upper(root.get("recStatus")),productModel.getRecStatus().toUpperCase()));
        }

        cq.orderBy(cb.asc(root.get("id")));

        return  p;

    }
}
