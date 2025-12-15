package K23CNTT3_Projeck3.Admin;

import K23CNTT3_Projeck3.entity.*;
import K23CNTT3_Projeck3.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    // PHƯƠNG THỨC KIỂM TRA ADMIN
    private boolean checkAdmin(HttpSession session, Model model) {
        System.out.println("=== 🔍 KIỂM TRA ADMIN ===");
        User currentUser = (User) session.getAttribute("currentUser");
        System.out.println("👤 User từ session: " + currentUser);

        if (currentUser == null) {
            System.out.println("❌ CHƯA ĐĂNG NHẬP");
            return false;
        }

        System.out.println("📧 Email: " + currentUser.getEmail());
        System.out.println("👤 Role: " + currentUser.getRole());
        System.out.println("🔑 Role name: " + currentUser.getRole().name());

        boolean isAdmin = "ADMIN".equals(currentUser.getRole().name());
        System.out.println("👑 Is ADMIN: " + isAdmin);

        if (!isAdmin) {
            System.out.println("❌ KHÔNG PHẢI ADMIN");
            return false;
        }

        model.addAttribute("currentUser", currentUser);
        System.out.println("✅ KIỂM TRA ADMIN THÀNH CÔNG");
        return true;
    }

    @GetMapping("")
    public String adminHome(HttpSession session, Model model) {
        System.out.println("=== 🏠 TRANG CHỦ ADMIN ===");
        return "redirect:/admin/dashboard";
    }

    // Dashboard Admin - ĐÃ SỬA HOÀN TOÀN
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        System.out.println("=== 🎯 TRUY CẬP ADMIN DASHBOARD ===");

        // KIỂM TRA TRỰC TIẾP
        User currentUser = (User) session.getAttribute("currentUser");
        System.out.println("👤 User từ session: " + currentUser);

        if (currentUser == null) {
            System.out.println("❌ CHƯA ĐĂNG NHẬP - CHUYỂN HƯỚNG ĐẾN LOGIN");
            return "redirect:/users/login";
        }

        System.out.println("📧 Email: " + currentUser.getEmail());
        System.out.println("👤 Role: " + currentUser.getRole().name());

        if (!"ADMIN".equals(currentUser.getRole().name())) {
            System.out.println("❌ KHÔNG CÓ QUYỀN ADMIN - CHUYỂN HƯỚNG VỀ TRANG CHỦ");
            return "redirect:/";
        }

        System.out.println("✅ CÓ QUYỀN ADMIN - HIỂN THỊ DASHBOARD");

        try {
            // DÙNG DỮ LIỆU MẪU ĐỂ TEST
            model.addAttribute("totalProducts", 15);
            model.addAttribute("totalUsers", 8);
            model.addAttribute("totalOrders", 25);
            model.addAttribute("totalCategories", 5);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("activeMenu", "dashboard");

            System.out.println("🎉 TRẢ VỀ TEMPLATE: admin/dashboard");
            return "admin/dashboard";
        } catch (Exception e) {
            System.out.println("❌ LỖI: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    // 🔥 DEBUG METHOD - KIỂM TRA SESSION
    @GetMapping("/test-session")
    @ResponseBody
    public String testSession(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "❌ KHÔNG CÓ USER TRONG SESSION - Session ID: " + session.getId();
        }

        return "✅ CÓ USER TRONG SESSION - " +
                "Email: " + currentUser.getEmail() +
                ", Role: " + currentUser.getRole().name() +
                ", Session ID: " + session.getId();
    }

    // 🔥 DEBUG METHOD - TEST ĐƠN GIẢN NHẤT
    @GetMapping("/simple")
    @ResponseBody
    public String simpleAdmin(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "❌ CHƯA ĐĂNG NHẬP";
        }

        return "✅ ĐÃ ĐĂNG NHẬP: " + currentUser.getEmail() + " | Role: " + currentUser.getRole().name();
    }

    // 🔥 DEBUG METHOD - TRANG TEST CHI TIẾT
    @GetMapping("/debug")
    @ResponseBody
    public String debugAdmin(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        StringBuilder result = new StringBuilder();
        result.append("<h1>🔍 DEBUG ADMIN</h1>");
        result.append("<h3>Thông tin session:</h3>");
        result.append("<p>Session ID: ").append(session.getId()).append("</p>");
        result.append("<p>CurrentUser: ").append(currentUser).append("</p>");

        if (currentUser != null) {
            result.append("<p style='color: green;'>✅ CÓ USER TRONG SESSION</p>");
            result.append("<p>Email: ").append(currentUser.getEmail()).append("</p>");
            result.append("<p>Role: ").append(currentUser.getRole()).append("</p>");
            result.append("<p>Role Name: ").append(currentUser.getRole().name()).append("</p>");
            result.append("<p>Is ADMIN: ").append("ADMIN".equals(currentUser.getRole().name())).append("</p>");
            result.append("<p><a href='/admin/dashboard' style='color: green;'>➡️ Đi đến Dashboard</a></p>");
        } else {
            result.append("<p style='color: red;'>❌ CHƯA ĐĂNG NHẬP</p>");
            result.append("<p><a href='/users/login'>🔑 Đăng nhập</a></p>");
        }

        return result.toString();
    }

    // QUẢN LÝ SẢN PHẨM
    @GetMapping("/products")
    public String manageProducts(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }

        try {
            List<Product> products = productService.getAllProducts();
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("activeMenu", "products");
            return "admin/products";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải sản phẩm: " + e.getMessage());
            model.addAttribute("activeMenu", "products");
            return "admin/products";
        }
    }

    @GetMapping("/products/add")
    public String showAddProductForm(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activeMenu", "products");
        return "admin/add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(HttpSession session, @ModelAttribute Product product) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(HttpSession session, @PathVariable Long id, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        Optional<Product> product = productService.getProductByIdOptional(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("activeMenu", "products");
            return "admin/edit-product";
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(HttpSession session, @PathVariable Long id, @ModelAttribute Product product) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(HttpSession session, @PathVariable Long id) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    // QUẢN LÝ NGƯỜI DÙNG
    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("activeMenu", "users");
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(HttpSession session, @PathVariable Long id, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        Optional<User> user = userService.getUserByIdOptional(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("activeMenu", "users");
            return "admin/edit-user";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(HttpSession session, @PathVariable Long id, @ModelAttribute User user) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        user.setId(id);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(HttpSession session, @PathVariable Long id) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // QUẢN LÝ ĐƠN HÀNG
    @GetMapping("/orders")
    public String manageOrders(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        List<Order> orders = orderService.getAllOrders();
        long pendingCount = orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long completedCount = orders.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).count();
        model.addAttribute("orders", orders);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("activeMenu", "orders");
        return "admin/orders";
    }

    @GetMapping("/orders/detail/{id}")
    public String orderDetail(HttpSession session, @PathVariable Long id, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        Optional<Order> order = orderService.getOrderByIdOptional(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("activeMenu", "orders");
            return "admin/order-detail";
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/update-status/{id}")
    public String updateOrderStatus(HttpSession session, @PathVariable Long id, @RequestParam OrderStatus status) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    // QUẢN LÝ DANH MỤC
    @GetMapping("/categories")
    public String manageCategories(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("activeMenu", "categories");
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(HttpSession session, @RequestParam String name) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        Category category = new Category();
        category.setName(name);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(HttpSession session, @PathVariable Long id) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    // QUẢN LÝ ĐÁNH GIÁ - SỬA THÀNH POST METHOD
    @GetMapping("/reviews")
    public String manageReviews(HttpSession session, Model model) {
        if (!checkAdmin(session, model)) {
            return "redirect:/users/login";
        }
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("activeMenu", "reviews");
        return "admin/reviews";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!checkAdmin(session, new org.springframework.ui.ExtendedModelMap())) {
            return "redirect:/users/login";
        }
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa đánh giá: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}