package K23CNTT3_Projeck3.repository;

import K23CNTT3_Projeck3.entity.Review;
import K23CNTT3_Projeck3.entity.Product;
import K23CNTT3_Projeck3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
    Optional<Review> findByProductAndUser(Product product, User user);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product")
    Double getAverageRatingByProduct(@Param("product") Product product);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product = :product")
    Long getReviewCountByProduct(@Param("product") Product product);

    List<Review> findByRating(Integer rating);
}