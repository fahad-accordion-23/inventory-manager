package ledge.inventory.writemodel.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testRegisterProduct() {
        Product p = Product.register("Laptop", new BigDecimal("800"), new BigDecimal("1200"), 10, new BigDecimal("0.1"));
        
        assertNotNull(p.getId());
        assertEquals("Laptop", p.getName());
        assertEquals(new BigDecimal("800"), p.getPurchasePrice());
        assertEquals(new BigDecimal("1200"), p.getSellingPrice());
        assertEquals(10, p.getStockQuantity());
        assertEquals(new BigDecimal("0.1"), p.getTaxRate());
    }

    @Test
    void testValidationConstraints() {
        assertThrows(IllegalArgumentException.class, () -> Product.register("", new BigDecimal("100"), new BigDecimal("150"), 10, new BigDecimal("0.1")));
        assertThrows(IllegalArgumentException.class, () -> Product.register("Valid", new BigDecimal("-10"), new BigDecimal("150"), 10, new BigDecimal("0.1")));
        assertThrows(IllegalArgumentException.class, () -> Product.register("Valid", new BigDecimal("100"), new BigDecimal("150"), -1, new BigDecimal("0.1")));
        assertThrows(IllegalArgumentException.class, () -> Product.register("Valid", new BigDecimal("100"), new BigDecimal("150"), 10, new BigDecimal("1.5")));
    }

    @Test
    void testSettersAndImmutability() {
        Product p = Product.register("Orig", new BigDecimal("100"), new BigDecimal("150"), 10, new BigDecimal("0.1"));
        p.setName("New Name");
        p.setStockQuantity(5);
        
        assertEquals("New Name", p.getName());
        assertEquals(5, p.getStockQuantity());
    }

    @Test
    void testTaxRateDefaults() {
        Product p = Product.register("No Tax", new BigDecimal("100"), new BigDecimal("150"), 10, null);
        assertEquals(BigDecimal.ZERO, p.getTaxRate());
    }
}
