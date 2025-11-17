package k23cnt3.tvtday7.controller;

import k23cnt3.tvtday7.entity.tvtCategory;
import k23cnt3.tvtday7.service.tvtCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class tvtCategoryController {

    @Autowired
    private tvtCategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        System.out.println("=== 🚀 ACCESSING CATEGORY LIST ===");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "tvtcategory-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        System.out.println("=== 📝 ACCESSING CREATE FORM ===");
        model.addAttribute("category", new tvtCategory());
        return "tvtcategory-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        System.out.println("=== ✏️ ACCESSING EDIT FORM FOR ID: " + id + " ===");
        model.addAttribute("category", categoryService.getLvsCategoryById(id).orElse(null));
        return "tvtcategory-form";
    }

    // ✅ SỬA: Gộp cả create và update vào 1 endpoint
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute tvtCategory category) {
        System.out.println("=== 💾 SAVING CATEGORY: " + category.getCategoryName() + " ===");
        categoryService.saveLvsCategory(category);
        return "redirect:/category";
    }

    // ✅ HOẶC: Giữ riêng biệt nhưng dùng method khác nhau
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute tvtCategory category) {
        System.out.println("=== 🔄 UPDATING CATEGORY ID: " + id + " ===");
        category.setId(id);
        categoryService.saveLvsCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        System.out.println("=== 🗑️ DELETING CATEGORY ID: " + id + " ===");
        categoryService.deleteLvsCategory(id);
        return "redirect:/category";
    }
}