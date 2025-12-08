package K23CNTT_tvtDay08.controller;

import K23CNTT_tvtDay08.Entity.Author;
import K23CNTT_tvtDay08.Entity.Book;
import K23CNTT_tvtDay08.repository.AuthorRepository;
import K23CNTT_tvtDay08.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    // Hiển thị danh sách sách
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepo.findAll());
        return "book-list";
    }

    // Hiển thị form thêm mới sách
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepo.findAll());
        return "book-form";
    }

    // Xử lý khi submit form thêm mới
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book,
                          @RequestParam List<Long> authorIds,
                          @RequestParam(required = false) Long editorId) {
        List<Author> selectedAuthors = authorRepo.findAllById(authorIds);
        book.setAuthors(selectedAuthors);
        book.setEditorId(editorId);
        bookRepo.save(book);
        return "redirect:/books";
    }
}