package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.ProductSize;
import K23CNTT3_Projeck3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    List<ProductSize> findByProduct(Product product);
    Optional<ProductSize> findByProductAndSize(Product product, Integer size);
}