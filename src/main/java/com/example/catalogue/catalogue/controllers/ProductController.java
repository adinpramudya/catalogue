package com.example.catalogue.catalogue.controllers;

import com.example.catalogue.catalogue.entities.ProductEntity;
import com.example.catalogue.catalogue.exceptions.ClientException;
import com.example.catalogue.catalogue.exceptions.NotFoundException;
import com.example.catalogue.catalogue.models.ProductModel;
import com.example.catalogue.catalogue.models.ResponseModel;
import com.example.catalogue.catalogue.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseModel> postProductController(@RequestBody ProductModel productModel) {
        try {
            ProductEntity product = productService.add(productModel);
            ResponseModel response = new ResponseModel();
            response.setMsg("New Product is successfully added");
            response.setData(product);

            return ResponseEntity.ok(response);
        } catch (ClientException e) {
            ResponseModel response = new ResponseModel();
            response.setMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseModel response = new ResponseModel();
            response.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseModel> getAllProductController() {
        try {
            List<ProductEntity> products = productService.findAll();
            ResponseModel res = new ResponseModel();
            res.setMsg("Request Successfully");
            res.setData(products);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ResponseModel res = new ResponseModel();
            res.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(res);
        }
    }

    @GetMapping("/get/search")
    public ResponseEntity<ResponseModel> searchProductController(@RequestBody ProductModel productModel) {
        try {
            List<ProductEntity> products = productService.findAllByCriteria(productModel);
            ResponseModel res = new ResponseModel();
            res.setMsg("Request Successfully");
            res.setData(products);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ResponseModel res = new ResponseModel();
            res.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(res);

        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseModel> getProductByIdController(@PathVariable Integer id) {
        try {
            ProductEntity product = productService.findById(id);
            ResponseModel res = new ResponseModel();
            res.setMsg("Request Successfully");
            res.setData(product);
            return ResponseEntity.ok(res);
        } catch (ClientException e) {
            ResponseModel response = new ResponseModel();
            response.setMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseModel response = new ResponseModel();
            response.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModel> putProductController(@RequestBody ProductModel productModel) {
        try {
            ProductEntity product = productService.edit(productModel);
            ResponseModel res = new ResponseModel();
            res.setMsg("Product is Successfully updated");
            res.setData(product);
            return ResponseEntity.ok(res);
        } catch (ClientException e) {
            ResponseModel response = new ResponseModel();
            response.setMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseModel response = new ResponseModel();
            response.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseModel> deleteProductController(@RequestBody ProductModel productModel) {
        try {
            ProductEntity product = productService.delete(productModel);
            ResponseModel res = new ResponseModel();
            res.setMsg("Product is Successfully deleted");
            res.setData(product);
            return ResponseEntity.ok(res);
        } catch (ClientException e) {
            ResponseModel response = new ResponseModel();
            response.setMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (NotFoundException e) {
            ResponseModel response = new ResponseModel();
            response.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseModel response = new ResponseModel();
            response.setMsg("Sorry, there is a failure on our server");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
