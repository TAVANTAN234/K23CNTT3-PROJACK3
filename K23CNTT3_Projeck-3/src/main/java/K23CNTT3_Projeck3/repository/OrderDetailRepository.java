package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.OrderDetail;
import K23CNTT3_Projeck3.entity.Order;
import K23CNTT3_Projeck3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // THÊM IMPORT NÀY
import org.springframework.data.repository.query.Param; // THÊM IMPORT NÀY
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
    List<OrderDetail> findByProduct(Product product);

    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.product = :product")
    Long getTotalSoldQuantityByProduct(@Param("product") Product product);
}