package com.bittercode.controller;

import com.bittercode.model.Book;
import com.bittercode.model.Cart;
import com.bittercode.model.Order;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.OrderService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.service.impl.OrderServiceImpl;
import com.bittercode.util.StoreUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerBookController {

    private final BookService bookService = new BookServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    // =========================================================
    //  /viewbook  — browse all books
    // =========================================================
    @GetMapping("/viewbook")
    public String viewBooks(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession(false);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        StoreUtil.updateCartItems(req);
        try {
            List<Book> books = bookService.getAllBooks();
            model.addAttribute("books", books);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to load books.");
        }
        return "viewbook";
    }

    @PostMapping("/viewbook")
    public String viewBooksPost(HttpServletRequest req, Model model) {
        // POST is used to add/remove from cart via updateCartItems
        return viewBooks(req, model);
    }

    // =========================================================
    //  /searchbook
    // =========================================================
    @GetMapping("/searchbook")
    public String searchBooks(@RequestParam(value = "query", required = false) String query,
                              HttpServletRequest req, Model model) {
        HttpSession session = req.getSession(false);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        model.addAttribute("query", query);
        if (query != null && !query.trim().isEmpty()) {
            StoreUtil.updateCartItems(req);
            try {
                List<Book> books = bookService.searchBooks(query.trim());
                model.addAttribute("books", books);
            } catch (Exception e) {
                model.addAttribute("errorMsg", "Search failed.");
            }
        }
        return "searchbook";
    }

    // =========================================================
    //  /cart
    // =========================================================
    @GetMapping("/cart")
    public String cart(HttpServletRequest req, Model model) {
        return loadCart(req, model);
    }

    @PostMapping("/cart")
    public String cartPost(HttpServletRequest req, Model model) {
        return loadCart(req, model);
    }

    private String loadCart(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession(false);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        StoreUtil.updateCartItems(req);
        try {
            String bookIds = session.getAttribute("items") != null
                    ? (String) session.getAttribute("items") : "";
            List<Book> books = bookService.getBooksByCommaSeperatedBookIds(bookIds);
            List<Cart> cartItems = new ArrayList<>();
            double amountToPay = 0;
            for (Book book : books) {
                Object qtyAttr = session.getAttribute("qty_" + book.getBarcode());
                int qty = qtyAttr != null ? (int) qtyAttr : 0;
                cartItems.add(new Cart(book, qty));
                amountToPay += qty * book.getPrice();
            }
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("amountToPay", amountToPay);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("amountToPay", amountToPay);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to load cart.");
        }
        return "cart";
    }

    // =========================================================
    //  /checkout
    // =========================================================
    @PostMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        model.addAttribute("amountToPay", session.getAttribute("amountToPay"));
        return "payment";
    }

    // =========================================================
    //  /pay
    // =========================================================
    @PostMapping("/pay")
    public String processPayment(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        try {
            String customerEmail = (String) session.getAttribute("CUSTOMER");
            List<Cart> cartItems = new ArrayList<>();
            if (session.getAttribute("cartItems") != null) {
                cartItems = (List<Cart>) session.getAttribute("cartItems");
            }
            if (cartItems.isEmpty()) {
                model.addAttribute("errorMsg", "No items in cart.");
                return "cart";
            }
            List<Book> orderedBooks = new ArrayList<>();
            for (Cart cart : cartItems) {
                Book book = cart.getBook();
                int newQty = book.getQuantity() - cart.getQuantity();
                bookService.updateBookQtyById(book.getBarcode(), newQty);
                Order order = new Order(customerEmail, book.getBarcode(), book.getName(),
                        book.getAuthor(), book.getPrice(), cart.getQuantity());
                orderService.saveOrder(order);
                orderedBooks.add(book);
                session.removeAttribute("qty_" + book.getBarcode());
            }
            session.removeAttribute("amountToPay");
            session.removeAttribute("cartItems");
            session.removeAttribute("items");
            session.removeAttribute("selectedBookId");
            model.addAttribute("orderedBooks", orderedBooks);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Payment processing failed.");
        }
        return "receipt";
    }

    // =========================================================
    //  /orders
    // =========================================================
    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, session)) {
            model.addAttribute("errorMsg", "Please login first to continue!");
            return "CustomerLogin";
        }
        try {
            String customerEmail = (String) session.getAttribute("CUSTOMER");
            List<Order> orders = orderService.getOrdersByUsername(customerEmail);
            model.addAttribute("orders", orders);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Failed to load orders.");
        }
        return "orders";
    }
}
