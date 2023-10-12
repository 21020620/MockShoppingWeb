package com.example.demo.product_service.entity;

import com.example.demo.customer_service.entity.Customer;
import jakarta.persistence.*;

@Entity
public class ProductReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewText;

    @OneToOne
    private Customer reviewer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double rating;

    public ProductReview() {
    }

    public ProductReview(String reviewText, Customer reviewer, Product product, double rating) {
        this.reviewText = reviewText;
        this.reviewer = reviewer;
        this.product = product;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Customer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Customer reviewer) {
        this.reviewer = reviewer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Customer " + reviewer.getName() + " rated " + rating + " stars and said:\n" + reviewText;
    }
}
