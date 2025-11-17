package k23cnt3.tvtday7.repository;

import k23cnt3.tvtday7.entity.tvtProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface tvtProductRepository extends
        JpaRepository<tvtProduct, Long> {
}
