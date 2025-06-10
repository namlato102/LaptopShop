package com.namlato.laptopshop.service.specification;

import org.springframework.data.jpa.domain.Specification;
import com.namlato.laptopshop.domain.Product;
import com.namlato.laptopshop.domain.Product_;

import java.util.List;

public class ProductSpecs {
    //filter by multiple factories (apple, dell, hp, etc.)
    public static Specification<Product> matchListFactory(List<String> factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
    }

    //filter by list of targets (gaming, business, etc.)
    public static Specification<Product> matchListTarget(List<String> target) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.TARGET)).value(target);
    }

    //filter by multiple price range
    public static Specification<Product> matchMultiplePrice(double min, double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get(Product_.PRICE), min, max);
    }
}
