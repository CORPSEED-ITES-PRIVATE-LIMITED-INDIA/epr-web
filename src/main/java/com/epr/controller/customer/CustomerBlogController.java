package com.epr.controller.customer;

import com.epr.dto.customer.BlogCustomerDto;
import com.epr.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
@CrossOrigin(origins = "*")
public class CustomerBlogController {

    @Autowired
    private BlogService blogService;

    // All active & visible blogs
    @GetMapping
    public ResponseEntity<List<BlogCustomerDto>> getAllActiveBlogs() {
        List<BlogCustomerDto> blogs = blogService.findAllPublicBlogs();
        return ResponseEntity.ok(blogs);
    }

    // Detail by slug (SEO friendly)
    @GetMapping("/{slug}")
    public ResponseEntity<BlogCustomerDto> getBlogBySlug(@PathVariable String slug) {
        BlogCustomerDto blog = blogService.findPublicBySlugAndIncrementVisit(slug);
        return blog != null ? ResponseEntity.ok(blog) : ResponseEntity.notFound().build();
    }

    // Latest 10 blogs
    @GetMapping("/latest")
    public ResponseEntity<List<BlogCustomerDto>> getLatestBlogs() {
        return ResponseEntity.ok(blogService.findLatestPublicBlogs(10));
    }

    // By category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BlogCustomerDto>> getBlogsByCategory(@PathVariable Long categoryId) {
        List<BlogCustomerDto> blogs = blogService.findPublicByCategoryId(categoryId);
        return ResponseEntity.ok(blogs);
    }

    // By subcategory
    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<BlogCustomerDto>> getBlogsBySubcategory(@PathVariable Long subcategoryId) {
        List<BlogCustomerDto> blogs = blogService.findPublicBySubcategoryId(subcategoryId);
        return ResponseEntity.ok(blogs);
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<BlogCustomerDto>> search(@RequestParam String q) {
        return ResponseEntity.ok(blogService.searchPublicBlogs(q.trim()));
    }

    // Featured blogs (show on home)
    @GetMapping("/featured")
    public ResponseEntity<List<BlogCustomerDto>> getFeaturedBlogs() {
        return ResponseEntity.ok(blogService.findFeaturedPublicBlogs());
    }
}