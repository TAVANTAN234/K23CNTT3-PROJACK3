package k23cnt3.tvtday7.service;

import k23cnt3.tvtday7.entity.tvtProduct;
import k23cnt3.tvtday7.repository.tvtProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service

public class tvtProductService {
    @Autowired
    private tvtProductRepository productRepository;
    // Đọc toàn bộ dữ liệu bảng LvsProduct
    public List<tvtProduct> getAllLvsProducts() {
        return productRepository.findAll();
    }
    // Đọc dữ liệu bảng LvsProduct theo id
    public Optional<tvtProduct> findById(Long id) {
        return productRepository.findById(id);
    }
    // Cập nhật: create / update
    public tvtProduct saveLvsProduct(tvtProduct product) {
        System.out.println(product);
        return productRepository.save(product);
    }
    // Xóa product theo id
    public void deleteLvsProduct(Long id) {
        productRepository.deleteById(id);
    }
}
