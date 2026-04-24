package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.domain.Product;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

import org.springframework.stereotype.Service;

@Service
public class AddProductCommandHandler implements CommandHandler<AddProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final IProductReadRepository readProductRepository;

    public AddProductCommandHandler(IProductWriteRepository writeProductRepository,
            IProductReadRepository readProductRepository) {
        this.writeProductRepository = writeProductRepository;
        this.readProductRepository = readProductRepository;
    }

    @Override
    public void handle(AddProductCommand command) {
        Product product = Product.register(
                command.name(),
                command.purchasePrice(),
                command.sellingPrice(),
                command.stockQuantity(),
                command.taxRate());

        writeProductRepository.save(product);
        readProductRepository.save(ProductDTO.fromProduct(product));
    }
}
