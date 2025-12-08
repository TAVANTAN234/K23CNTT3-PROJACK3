package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.Product;
import K23CNTT3_Projeck3.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    List<Product> findByStockGreaterThan(Integer stock);

    @Query("SELECT p FROM Product p ORDER BY p.id DESC LIMIT :count")
    List<Product> findNewestProducts(@Param("count") int count);
}