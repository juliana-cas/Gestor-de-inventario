package cue.edu.Inventario;

import com.example.inventorymanagement.controller.ProductController;
import com.example.inventorymanagement.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController productController;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId("1");
        product.setName("Producto A");
        product.setCategory("Categoría A");
        product.setPrice(10.0);
        product.setQuantity(100);
    }

    @Test
    public void testAddProduct() throws Exception {
        mockMvc.perform(post("/products/add")
                        .param("id", product.getId())
                        .param("name", product.getName())
                        .param("category", product.getCategory())
                        .param("price", String.valueOf(product.getPrice()))
                        .param("quantity", String.valueOf(product.getQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // Verificar que el producto se haya añadido
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto A")));
    }

    @Test
    public void testDeleteNonExistentProduct() throws Exception {
        // Intentar eliminar un producto que no existe
        mockMvc.perform(get("/products/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // Verificar que la lista de productos no cambió
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto A")));
    }

    @Test
    public void testSearchWithoutResults() throws Exception {
        mockMvc.perform(get("/products")
                        .param("search", "Producto Inexistente"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No se encontraron resultados"))); // Asumiendo que tienes una lógica de no resultados.
    }

    @Test
    public void testEditNonExistentProduct() throws Exception {
        // Intentar editar un producto que no existe
        mockMvc.perform(post("/products/edit/999")
                        .param("id", "999")
                        .param("name", "Producto Inexistente")
                        .param("category", "Categoría Inexistente")
                        .param("price", "0")
                        .param("quantity", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // Verificar que el producto original no cambió
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto A")));
    }
}
