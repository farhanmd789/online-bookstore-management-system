package com.bittercode.controller;

import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminBookController {

    private final BookService bookService = new BookServiceImpl();

    // =========================================================
    //  /storebooks
    // =========================================================
    @GetMapping("/storebooks")
    public String storeBooks(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        try {
            List<Book> books = bookService.getAllBooks();
            model.addAttribute("books", books);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to load books.");
        }
        return "storebooks";
    }

    // =========================================================
    //  /addbook
    // =========================================================
    @GetMapping("/addbook")
    public String showAddBook(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        return "addbook";
    }

    @PostMapping("/addbook")
    public String addBook(@RequestParam("name") String name,
                          @RequestParam("author") String author,
                          @RequestParam("price") String priceStr,
                          @RequestParam("quantity") String quantityStr,
                          HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        try {
            String bCode = bookService.getNextBookId();
            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(quantityStr);
            Book book = new Book(bCode, name, author, price, qty);
            String result = bookService.addBook(book);
            if ("SUCCESS".equalsIgnoreCase(result)) {
                model.addAttribute("successMsg", "Book added successfully! (ID: " + bCode + ")");
            } else {
                model.addAttribute("errorMsg", "Failed to add book.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to add book: " + e.getMessage());
        }
        return "addbook";
    }

    // =========================================================
    //  /updatebook
    // =========================================================
    @GetMapping("/updatebook")
    public String showUpdateBook(@RequestParam(value = "bookId", required = false) String bookId,
                                 HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        if (bookId != null && !bookId.isBlank()) {
            try {
                Book book = bookService.getBookById(bookId);
                model.addAttribute("book", book);
            } catch (Exception e) {
                model.addAttribute("errorMsg", "Book not found.");
            }
        }
        return "updatebook";
    }

    @PostMapping("/updatebook")
    public String updateBook(@RequestParam(value = "barcode", required = false) String barcode,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "author", required = false) String author,
                             @RequestParam(value = "price", required = false) String priceStr,
                             @RequestParam(value = "quantity", required = false) String quantityStr,
                             @RequestParam(value = "bookId", required = false) String bookIdParam,
                             HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        // If bookId param present (from storebooks table), redirect to GET
        if (bookIdParam != null && !bookIdParam.isBlank() && barcode == null) {
            return "redirect:/updatebook?bookId=" + bookIdParam;
        }
        try {
            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(quantityStr);
            Book book = new Book(barcode, name, author, price, qty);
            String result = bookService.updateBook(book);
            if ("SUCCESS".equalsIgnoreCase(result)) {
                model.addAttribute("successMsg", "Book updated successfully!");
                model.addAttribute("book", book);
            } else {
                model.addAttribute("errorMsg", "Failed to update book.");
                model.addAttribute("book", book);
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to update book: " + e.getMessage());
        }
        return "updatebook";
    }

    // =========================================================
    //  /removebook
    // =========================================================
    @GetMapping("/removebook")
    public String showRemoveBook(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        return "removebook";
    }

    @PostMapping("/removebook")
    public String removeBook(@RequestParam(value = "bookId", required = false) String bookId,
                             HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "SellerLogin";
        }
        if (bookId == null || bookId.isBlank()) {
            return "removebook";
        }
        try {
            String result = bookService.deleteBookById(bookId.trim());
            if ("SUCCESS".equalsIgnoreCase(result)) {
                model.addAttribute("successMsg", "Book removed successfully.");
            } else {
                model.addAttribute("errorMsg", "Book not found in store.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to remove book.");
        }
        return "removebook";
    }
}
