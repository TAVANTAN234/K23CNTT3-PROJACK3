package K23CNTT3_Projeck3.controller;

import K23CNTT3_Projeck3.entity.Product;
import K23CNTT3_Projeck3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcTemplate jdbcTemplate; // THÊM DÒNG NÀY

    @GetMapping("/")
    public String home(Model model) {
        List<Product> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("pageTitle", "Sneaker Shop - Trang chủ");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Về chúng tôi");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên hệ");
        return "contact";
    }

    // THÊM CÁC ENDPOINT TEST DƯỚI ĐÂY

    @GetMapping("/test-db")
    @ResponseBody
    public String testDatabase() {
        try {
            String result = jdbcTemplate.queryForObject("SELECT 'Kết nối database THÀNH CÔNG!' as message", String.class);
            return result;
        } catch (Exception e) {
            return "❌ Lỗi kết nối database: " + e.getMessage();
        }
    }

    @GetMapping("/test-users")
    @ResponseBody
    public String testUsersTable() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            return "✅ Số users trong database: " + count;
        } catch (Exception e) {
            return "❌ Lỗi truy vấn users: " + e.getMessage();
        }
    }

    @GetMapping("/test-products")
    @ResponseBody
    public String testProductsTable() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product", Integer.class);
            return "✅ Số products trong database: " + count;
        } catch (Exception e) {
            return "❌ Lỗi truy vấn products: " + e.getMessage();
        }
    }
}