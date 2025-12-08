package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.Cart;
import K23CNTT3_Projeck3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    boolean existsByUser(User user);
}