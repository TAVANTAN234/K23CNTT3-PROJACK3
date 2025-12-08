package K23CNTT3_Projeck3.service;

import K23CNTT3_Projeck3.entity.User;
import K23CNTT3_Projeck3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // THÊM METHOD MỚI CHO ADMIN
    public Optional<User> getUserByIdOptional(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        return userRepository.save(existingUser);
    }

    // THÊM METHOD MỚI CHO ADMIN
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User login(String email, String password) {
        System.out.println("=== DEBUG LOGIN START ===");
        System.out.println("Email nhập: '" + email + "'");
        System.out.println("Password nhập: '" + password + "'");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            System.out.println("❌ Email không tồn tại: " + email);
            throw new RuntimeException("Email không tồn tại!");
        }

        User user = userOptional.get();
        System.out.println("✅ Tìm thấy user: " + user.getName());
        System.out.println("Password trong DB: '" + user.getPassword() + "'");
        System.out.println("So sánh: '" + password + "' vs '" + user.getPassword() + "'");
        System.out.println("Kết quả so sánh: " + user.getPassword().equals(password));

        if (user.getPassword().equals(password)) {
            System.out.println("✅ Đăng nhập thành công!");
            System.out.println("=== DEBUG LOGIN END ===");
            return user;
        } else {
            System.out.println("❌ Mật khẩu không đúng!");
            System.out.println("=== DEBUG LOGIN END ===");
            throw new RuntimeException("Mật khẩu không đúng!");
        }
    }
}