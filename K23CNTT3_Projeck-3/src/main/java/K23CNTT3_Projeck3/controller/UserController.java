package K23CNTT3_Projeck3.controller;

import K23CNTT3_Projeck3.entity.User;
import K23CNTT3_Projeck3.entity.Role;
import K23CNTT3_Projeck3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Đăng ký tài khoản");
        return "register";
    }

    // Xử lý đăng ký với validation
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin!");
                return "register";
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                model.addAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "register";
            }

            User savedUser = userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/users/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi đăng ký: " + e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("pageTitle", "Đăng nhập");
        return "login";
    }

    // Xử lý đăng nhập - PHIÊN BẢN CUỐI CÙNG (DÙNG REDIRECT)
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== 🔍 DEBUG LOGIN BẮT ĐẦU ===");
            System.out.println("📧 Email: " + email);
            System.out.println("🔐 Password: " + password);

            // Validation
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Vui lòng nhập email!");
                return "login";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Vui lòng nhập mật khẩu!");
                return "login";
            }

            // Gọi service đăng nhập
            User user = userService.login(email, password);
            System.out.println("✅ Login service thành công");

            // DEBUG CHI TIẾT USER
            System.out.println("=== THÔNG TIN USER ===");
            System.out.println("🆔 ID: " + user.getId());
            System.out.println("📧 Email: " + user.getEmail());
            System.out.println("👤 Role: " + user.getRole());
            System.out.println("🔑 Role name: " + user.getRole().name());
            System.out.println("👑 Is ADMIN: " + user.getRole().name().equals("ADMIN"));

            // Lưu user vào session
            session.setAttribute("currentUser", user);
            session.setMaxInactiveInterval(30 * 60);

            // DEBUG: Kiểm tra session ngay lập tức
            User sessionUser = (User) session.getAttribute("currentUser");
            System.out.println("=== KIỂM TRA SESSION ===");
            System.out.println("💾 Session user: " + sessionUser);
            System.out.println("📧 Session user email: " + (sessionUser != null ? sessionUser.getEmail() : "NULL"));
            System.out.println("🔑 Session ID: " + session.getId());

            redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công!");

            // 🔥 QUAN TRỌNG: DÙNG REDIRECT
            System.out.println("=== 🔥 DÙNG REDIRECT ===");
            System.out.println("🔄 CHUYỂN HƯỚNG ĐẾN: /admin/dashboard");
            return "redirect:/admin/dashboard";

        } catch (Exception e) {
            System.out.println("❌ LỖI ĐĂNG NHẬP: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("email", email);
            return "login";
        }
    }

    // 🔥 DEBUG METHOD - KIỂM TRA FORM SUBMIT
    @PostMapping("/login-debug")
    @ResponseBody
    public String loginDebug(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session) {
        try {
            System.out.println("=== 🐛 DEBUG FORM SUBMIT ===");
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            User user = userService.login(email, password);
            session.setAttribute("currentUser", user);

            return "✅ DEBUG THÀNH CÔNG - User: " + user.getEmail() +
                    " | Role: " + user.getRole().name() +
                    " | <a href='/admin/dashboard'>Vào Dashboard</a>";

        } catch (Exception e) {
            return "❌ DEBUG LỖI: " + e.getMessage();
        }
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("=== 🔓 ĐĂNG XUẤT ===");
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Đăng xuất thành công!");
        return "redirect:/";
    }

    // Hiển thị thông tin cá nhân
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        User user = userService.getUserById(currentUser.getId());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Thông tin cá nhân");
        return "profile";
    }

    // Cập nhật thông tin cá nhân
    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute User user,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return "redirect:/users/login";
            }

            if (result.hasErrors()) {
                model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin!");
                return "profile";
            }

            user.setId(currentUser.getId());
            user.setPassword(userService.getUserById(currentUser.getId()).getPassword());

            User updatedUser = userService.updateUser(user);
            session.setAttribute("currentUser", updatedUser);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
            return "redirect:/users/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
            return "profile";
        }
    }

    // Trang dashboard cho user
    @GetMapping("/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "Trang cá nhân");
        return "user-dashboard";
    }

    // Đổi mật khẩu - Hiển thị form
    @GetMapping("/change-password")
    public String showChangePasswordForm(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("pageTitle", "Đổi mật khẩu");
        return "change-password";
    }

    // Đổi mật khẩu - Xử lý
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return "redirect:/users/login";
            }

            if (newPassword == null || newPassword.length() < 6) {
                model.addAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                return "change-password";
            }

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
                return "change-password";
            }

            User user = userService.getUserById(currentUser.getId());
            if (!user.getPassword().equals(currentPassword)) {
                model.addAttribute("errorMessage", "Mật khẩu hiện tại không đúng!");
                return "change-password";
            }

            user.setPassword(newPassword);
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
            return "redirect:/users/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi đổi mật khẩu: " + e.getMessage());
            return "change-password";
        }
    }

    // Danh sách users (chỉ admin)
    @GetMapping("/admin")
    public String userManagement(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getRole().name().equals("ADMIN")) {
            return "redirect:/";
        }
        return "redirect:/admin/users";
    }

    // TẠO USER ADMIN
    @GetMapping("/create-admin")
    @ResponseBody
    public String createAdmin() {
        try {
            // Kiểm tra xem admin đã tồn tại chưa
            try {
                User existingAdmin = userService.login("admin@example.com", "admin123");
                return "✅ Admin đã tồn tại!<br>" +
                        "Email: admin@example.com<br>" +
                        "Password: admin123<br>" +
                        "<a href='/users/login'>Đăng nhập ngay</a>";
            } catch (Exception e) {
                // Nếu không tồn tại thì tạo mới
            }

            User adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("admin123");
            adminUser.setRole(Role.ADMIN);

            userService.registerUser(adminUser);
            return "✅ Đã tạo user ADMIN thành công!<br>" +
                    "Email: admin@example.com<br>" +
                    "Password: admin123<br>" +
                    "<a href='/users/login'>Đăng nhập ngay</a>";
        } catch (Exception e) {
            return "❌ Lỗi tạo admin: " + e.getMessage();
        }
    }

    // KIỂM TRA SESSION
    @GetMapping("/check-session")
    @ResponseBody
    public String checkSession(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "❌ KHÔNG CÓ USER TRONG SESSION<br>" +
                    "Session ID: " + session.getId() + "<br>" +
                    "<a href='/users/login'>Đăng nhập</a>";
        } else {
            return "✅ CÓ USER TRONG SESSION:<br>" +
                    "Email: " + currentUser.getEmail() + "<br>" +
                    "Role: " + currentUser.getRole().name() + "<br>" +
                    "Session ID: " + session.getId() + "<br>" +
                    "<a href='/admin/dashboard'>Thử vào Admin Dashboard</a>";
        }
    }

    // 🔥 ĐĂNG NHẬP TỰ ĐỘNG VÀO ADMIN
    @GetMapping("/auto-login")
    public String autoLogin(HttpSession session) {
        try {
            System.out.println("=== 🤖 AUTO LOGIN BẮT ĐẦU ===");

            // Đăng nhập với admin
            User user = userService.login("admin@example.com", "admin123");
            System.out.println("✅ Đăng nhập thành công: " + user.getEmail());

            // Lưu session
            session.setAttribute("currentUser", user);

            // Kiểm tra session
            User checkUser = (User) session.getAttribute("currentUser");
            System.out.println("💾 Session check: " + (checkUser != null ? "THÀNH CÔNG" : "THẤT BẠI"));

            // Vào admin dashboard
            System.out.println("🔄 Chuyển hướng đến admin dashboard");
            return "redirect:/admin/dashboard";

        } catch (Exception e) {
            System.out.println("❌ Lỗi auto login: " + e.getMessage());
            return "redirect:/users/create-admin";
        }
    }

    // 🔥 TEST FORWARD
    @GetMapping("/test-forward")
    public String testForward(HttpSession session) {
        try {
            System.out.println("=== 🧪 TEST FORWARD ===");

            User user = userService.login("admin@example.com", "admin123");
            session.setAttribute("currentUser", user);

            System.out.println("✅ Đã lưu session, dùng forward");
            return "forward:/admin/dashboard";

        } catch (Exception e) {
            return "redirect:/users/create-admin";
        }
    }

    // Kiểm tra session cho các request
    @ModelAttribute
    public void checkUserSession(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
        }
    }
}