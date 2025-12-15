// language: java
    package K23CNTT3_Projeck3.controller;

    import K23CNTT3_Projeck3.entity.Cart;
    import K23CNTT3_Projeck3.entity.User;
    import K23CNTT3_Projeck3.service.CartService;
    import K23CNTT3_Projeck3.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import jakarta.servlet.http.HttpSession;
    import java.util.HashMap;
    import java.util.Map;

    @Controller
    @RequestMapping("/cart")
    public class CartController {

        @Autowired
        private CartService cartService;

        @Autowired
        private UserService userService;

        @GetMapping
        public String viewCart(Model model, HttpSession session) {
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                model.addAttribute("errorMessage", "Vui lòng đăng nhập để xem giỏ hàng!");
                return "redirect:/users/login";
            }

            Cart cart = cartService.getCartByUser(currentUser);
            model.addAttribute("cart", cart);
            model.addAttribute("pageTitle", "Giỏ hàng của bạn");
            return "cart";
        }

        @PostMapping("/add/{productId}")
        public String addToCart(@PathVariable Long productId,
                                @RequestParam(defaultValue = "1") Integer quantity,
                                HttpSession session,
                                Model model) {
            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    model.addAttribute("errorMessage", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
                    return "redirect:/users/login";
                }

                cartService.addToCart(currentUser, productId, quantity);
                model.addAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng!");
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Lỗi thêm vào giỏ hàng: " + e.getMessage());
            }
            return "redirect:/products";
        }

        @PostMapping("/update/{productId}")
        @ResponseBody
        public Map<String, Object> updateCartItem(@PathVariable Long productId,
                                                  @RequestParam Integer quantity,
                                                  HttpSession session) {
            Map<String, Object> response = new HashMap<>();

            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    response.put("success", false);
                    response.put("message", "Vui lòng đăng nhập!");
                    return response;
                }

                // Use existing service method name
                cartService.updateCartItemQuantity(currentUser, productId, quantity);

                // Lấy lại cart sau khi cập nhật
                Cart cart = cartService.getCartByUser(currentUser);

                response.put("success", true);
                response.put("message", "Đã cập nhật số lượng!");
                response.put("totalPrice", cart.getTotalPrice());
                response.put("cartItemCount", cart.getCartItems().size());

            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Lỗi cập nhật: " + e.getMessage());
            }
            return response;
        }

        @PostMapping("/remove/{productId}")
        @ResponseBody
        public Map<String, Object> removeFromCart(@PathVariable Long productId,
                                                  HttpSession session) {
            Map<String, Object> response = new HashMap<>();

            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    response.put("success", false);
                    response.put("message", "Vui lòng đăng nhập!");
                    return response;
                }

                cartService.removeFromCart(currentUser, productId);

                Cart cart = cartService.getCartByUser(currentUser);

                response.put("success", true);
                response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
                response.put("totalPrice", cart.getTotalPrice());
                response.put("cartItemCount", cart.getCartItems().size());

            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Lỗi xóa sản phẩm: " + e.getMessage());
            }
            return response;
        }

        @GetMapping("/checkout")
        public String checkout(Model model, HttpSession session) {
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) {
                model.addAttribute("errorMessage", "Vui lòng đăng nhập để thanh toán!");
                return "redirect:/users/login";
            }

            Cart cart = cartService.getCartByUser(currentUser);

            if (cart.getCartItems().isEmpty()) {
                model.addAttribute("errorMessage", "Giỏ hàng trống!");
                return "redirect:/cart";
            }

            model.addAttribute("cart", cart);
            model.addAttribute("user", currentUser);
            model.addAttribute("pageTitle", "Thanh toán");
            return "checkout";
        }

        @GetMapping("/count")
        @ResponseBody
        public Map<String, Object> getCartCount(HttpSession session) {
            Map<String, Object> response = new HashMap<>();

            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    response.put("count", 0);
                    response.put("message", "Chưa đăng nhập");
                    return response;
                }

                Cart cart = cartService.getCartByUser(currentUser);
                int count = cart.getCartItems().stream()
                        .mapToInt(item -> item.getQuantity())
                        .sum();

                response.put("count", count);
                response.put("success", true);

            } catch (Exception e) {
                response.put("count", 0);
                response.put("message", "Lỗi: " + e.getMessage());
                response.put("success", false);
            }
            return response;
        }

        @PostMapping("/clear")
        @ResponseBody
        public Map<String, Object> clearCart(HttpSession session) {
            Map<String, Object> response = new HashMap<>();

            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    response.put("success", false);
                    response.put("message", "Vui lòng đăng nhập!");
                    return response;
                }

                cartService.clearCart(currentUser);

                response.put("success", true);
                response.put("message", "Đã xóa toàn bộ giỏ hàng!");
                response.put("totalPrice", 0.0);
                response.put("cartItemCount", 0);

            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Lỗi xóa giỏ hàng: " + e.getMessage());
            }
            return response;
        }

        @PostMapping("/api/add/{productId}")
        @ResponseBody
        public Map<String, Object> addToCartAjax(@PathVariable Long productId,
                                                 @RequestParam(defaultValue = "1") Integer quantity,
                                                 HttpSession session) {
            Map<String, Object> response = new HashMap<>();

            try {
                User currentUser = (User) session.getAttribute("currentUser");

                if (currentUser == null) {
                    response.put("success", false);
                    response.put("message", "Vui lòng đăng nhập để thêm sản phẩm!");
                    return response;
                }

                cartService.addToCart(currentUser, productId, quantity);

                Cart cart = cartService.getCartByUser(currentUser);
                int count = cart.getCartItems().stream()
                        .mapToInt(item -> item.getQuantity())
                        .sum();

                response.put("success", true);
                response.put("message", "Đã thêm sản phẩm vào giỏ hàng!");
                response.put("cartItemCount", count);

            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Lỗi: " + e.getMessage());
            }
            return response;
        }
    }