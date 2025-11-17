package k23cnt3.tvtday7.service;

import k23cnt3.tvtday7.entity.tvtCategory;
import k23cnt3.tvtday7.repository.tvtCategoryRepository; // ← ĐÃ SỬA PACKAGE
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class tvtCategoryService {

    @Autowired
    private tvtCategoryRepository categoryRepository;

    public tvtCategoryService(tvtCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<tvtCategory> getAllCategories() {
        List<tvtCategory> categories = categoryRepository.findAll();
        System.out.println("=== DATABASE CATEGORIES: " + categories.size() + " ===");

        // Debug: in ra tất cả categories
        for (tvtCategory category : categories) {
            System.out.println("Category: " + category.getId() + " - " +
                    category.getCategoryName() + " - " +
                    category.getCategoryStatus());
        }

        return categories;
    }

    public Optional<tvtCategory> getLvsCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public tvtCategory saveLvsCategory(tvtCategory category) {
        return categoryRepository.save(category);
    }

    public void deleteLvsCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}