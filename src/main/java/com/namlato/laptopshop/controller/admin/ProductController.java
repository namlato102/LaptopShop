package com.namlato.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.namlato.laptopshop.domain.Product;
import com.namlato.laptopshop.service.UploadService;
import com.namlato.laptopshop.service.ProductService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    // create new product
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(@ModelAttribute("newProduct") @Valid Product product,
                                      BindingResult newProductBindingResult,
                                      @RequestParam("uploadFile") MultipartFile file) {
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        // upload image
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    // get all products
    @GetMapping("/admin/product")
    public String getProduct(
            Model model,
            @RequestParam("page") Optional<String> pageOptional) {
        // pass page parameter to url, default page = 1
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> listProducts = prs.getContent();
        model.addAttribute("products", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());
        return "admin/product/products";
    }

    // get product detail
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.fetchProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/product";
    }

    // update product
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.fetchProductById(id).get();
        model.addAttribute("currentProduct", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("currentProduct") @Valid Product currentProduct,
                                      BindingResult newProductBindingResult,
                                      @RequestParam("uploadFile") MultipartFile file) {
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product updatedProduct = this.productService.fetchProductById(currentProduct.getId()).get();
        if (updatedProduct != null) {
            // update new image
            // if file is not empty, update image
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                updatedProduct.setImage(img);
            }
            updatedProduct.setName(currentProduct.getName());
            updatedProduct.setPrice(currentProduct.getPrice());
            updatedProduct.setQuantity(currentProduct.getQuantity());
            updatedProduct.setDetailDesc(currentProduct.getDetailDesc());
            updatedProduct.setShortDesc(currentProduct.getShortDesc());
            updatedProduct.setFactory(currentProduct.getFactory());
            updatedProduct.setTarget(currentProduct.getTarget());
            this.productService.handleSaveProduct(updatedProduct);
        }
        return "redirect:/admin/product";
    }

    // delete product
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.fetchProductById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("currentProduct", currentProduct);
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(@ModelAttribute("currentProduct") Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/admin/product";
    }
}
