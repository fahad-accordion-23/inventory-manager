package ledge.api.inventory;

import ledge.api.inventory.dto.request.CreateProductRequestDTO;
import ledge.api.inventory.dto.response.GetAllProductsResponseDTO;
import ledge.api.inventory.dto.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryControllerTest {

    private InventoryController controller;
    private CommandBus commandBus;
    private QueryBus queryBus;

    @BeforeEach
    void setUp() {
        commandBus = mock(CommandBus.class);
        queryBus = mock(QueryBus.class);
        controller = new InventoryController(commandBus, queryBus);
    }

    @Test
    void testGetAllProducts() {
        ProductDTO product = new ProductDTO(UUID.randomUUID(), "Laptop", new BigDecimal("800"), new BigDecimal("1200"),
                10, new BigDecimal("0.1"));
        when(queryBus.dispatch(any(), eq("valid-token"))).thenReturn(List.of(product));

        ApiResponse<GetAllProductsResponseDTO> response = controller.getAllProducts("Bearer valid-token");

        assertTrue(response.success());
        assertEquals(1, response.data().products().size());
        assertEquals("Laptop", response.data().products().get(0).name());
    }

    @Test
    void testCreateProduct() {
        CreateProductRequestDTO request = new CreateProductRequestDTO(
                "Phone", new BigDecimal("500"), new BigDecimal("700"), 5, new BigDecimal("0.05"));

        ProductDTO product = new ProductDTO(UUID.randomUUID(), "Phone", new BigDecimal("500"), new BigDecimal("700"),
                5, new BigDecimal("0.05"));

        when(queryBus.dispatch(any(), eq("valid-token"))).thenReturn(List.of(product));

        ApiResponse<ProductResponseDTO> response = controller.createProduct("Bearer valid-token", request);

        assertTrue(response.success());
        assertEquals("Phone", response.data().name());
        verify(commandBus).dispatch(any(AddProductCommand.class), eq("valid-token"));
    }
}
