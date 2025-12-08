package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.Order;
import K23CNTT3_Projeck3.entity.User;
import K23CNTT3_Projeck3.entity.OrderStatus; // THÊM DÒNG NÀY
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.orderDate DESC") // SỬA createdAt → orderDate
    List<Order> findUserOrdersByDateDesc(@Param("user") User user);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);
}