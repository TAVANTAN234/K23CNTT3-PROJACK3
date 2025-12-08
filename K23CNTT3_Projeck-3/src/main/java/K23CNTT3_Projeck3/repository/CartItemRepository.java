package K23CNTT3_Projeck3.repository;  // QUAN TRỌNG: Package phải đúng

import K23CNTT3_Projeck3.entity.CartItem;
import K23CNTT3_Projeck3.entity.Cart;
import K23CNTT3_Projeck3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCartAndProduct(Cart cart, Product product);
    Long countByCart(Cart cart);
    boolean existsByCartAndProduct(Cart cart, Product product);
}