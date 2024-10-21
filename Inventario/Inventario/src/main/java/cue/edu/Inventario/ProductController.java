package cue.edu.Inventario;

import cue.edu.Inventario.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private List<Product> productList = new ArrayList<>();

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productList);
        return "product-list"; // Vista de listado de productos
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form"; // Vista para agregar productos
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productList.add(product);
        return "redirect:/products"; // Redirige al listado de productos
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Product product = findProductById(id);
        model.addAttribute("product", product);
        return "product-form"; // Vista para editar productos
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable String id, @ModelAttribute Product updatedProduct) {
        Product product = findProductById(id);
        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
        }
        return "redirect:/products"; // Redirige al listado de productos
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productList.removeIf(product -> product.getId().equals(id));
        return "redirect:/products"; // Redirige al listado de productos
    }

    private Product findProductById(String id) {
        return productList.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}
