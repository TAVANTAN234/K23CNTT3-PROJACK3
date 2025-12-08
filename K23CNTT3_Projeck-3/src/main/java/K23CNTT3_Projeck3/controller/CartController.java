package K23CNTT3_Projeck3.controller;

import K23CNTT3_Projeck3.entity.Cart;
import K23CNTT3_Projeck3.entity.User;
import K23CNTT3_Projeck3.service.CartService;
import K23CNTT3_Projeck3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewCart(Model model) {
        // Tạm thời dùng user ID 1, sau này sẽ lấy từ authentication
        User user = userService.getUserById(1L);
        Cart cart = cartService.getCartByUser(user);

        model.addAttribute("cart", cart);
        model.addAttribute("pageTitle", "Giỏ hàng của bạn");
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            Model model) {
        try {
            User user = userService.getUserById(1L);
            cartService.addToCart(user, productId, quantity);
            model.addAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi thêm vào giỏ hàng: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/update/{productId}")
    public String updateCartItem(@PathVariable Long productId,
                                 @RequestParam Integer quantity,
                                 Model model) {
        try {
            User user = userService.getUserById(1L);
            // Logic cập nhật số lượng
            model.addAttribute("successMessage", "Đã cập nhật giỏ hàng!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Model model) {
        try {
            User user = userService.getUserById(1L);
            cartService.removeFromCart(user, productId);
            model.addAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        User user = userService.getUserById(1L);
        Cart cart = cartService.getCartByUser(user);

        if (cart.getCartItems().isEmpty()) {
            model.addAttribute("errorMessage", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("pageTitle", "Thanh toán");
        return "checkout";
    }
}