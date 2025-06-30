package com.poly.controller;

import com.poly.dao.CategoryDAO;
import com.poly.dao.ProductDAO;
import com.poly.model.Category;
import com.poly.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AllProductsController {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping("/all-product")
    public String allProducts(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "keyword", required = false) String keyword) { // Thêm tham số keyword

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty()) {
            // Tìm kiếm theo keyword
            productPage = productDAO.findByProductNameContainingIgnoreCase(keyword, pageable); // Hoặc một phương thức
            // tìm kiếm khác phù hợp
            model.addAttribute("keyword", keyword); // Lưu keyword để hiển thị lại trong form tìm kiếm

        } else {
            // Hiển thị tất cả sản phẩm
            productPage = productDAO.findAll(pageable);

        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("breadcrumbCurrent", "Tất cả sản phẩm");

        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        return "all-products";
    }

    @PostMapping("/product/search")
    public String search_price(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam("min") Optional<Double> min,
                               @RequestParam("max") Optional<Double> max) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productPage = productDAO.findByPriceBetween(
                min.orElse(Double.MIN_VALUE), // Use default min value if not present
                max.orElse(Double.MAX_VALUE), // Use default max value if not present
                pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("min", min.orElse(null));
        model.addAttribute("max", max.orElse(null));

        return "all-products";
    }

    @GetMapping("/product/category/{id}")
    public String viewByCategory(@PathVariable("id") Integer categoryId,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, 10);

        // Lấy category đầy đủ từ DB
        Optional<Category> optionalCategory = categoryDAO.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            // Nếu không tìm thấy, có thể chuyển về /all-product hoặc hiển thị trang lỗi
            return "redirect:/all-product";
        }

        Category category = optionalCategory.get();

        // Lấy danh sách sản phẩm theo danh mục
        Page<Product> productPage = productDAO.findByCategory(category, pageable);

        // Đẩy dữ liệu cho view
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categories", categoryDAO.findAll());

        // Breadcrumb
        model.addAttribute("breadcrumbLevel2", "Danh mục sản phẩm");
        model.addAttribute("breadcrumbCurrent", category.getCategoryName());

        return "all-products";
    }


}
