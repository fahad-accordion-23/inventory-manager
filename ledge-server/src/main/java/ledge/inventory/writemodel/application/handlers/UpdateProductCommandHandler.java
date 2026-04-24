package ledge.inventory.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.inventory.writemodel.contracts.UpdateProductCommand;
import ledge.inventory.writemodel.domain.Product;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;

import org.springframework.stereotype.Service;

@Service
public class UpdateProductCommandHandler implements CommandHandler<UpdateProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final IProductReadRepository readProductRepository;

    public UpdateProductCommandHandler(IProductWriteRepository writeProductRepository,
            IProductReadRepository readProductRepository) {
        this.writeProductRepository = writeProductRepository;
        this.readProductRepository = readProductRepository;
    }

    @Override
    public void handle(UpdateProductCommand command) {
        Product updatedProduct = Product.rehydrate(
                command.id(),
                command.name(),
                command.purchasePrice(),
                command.sellingPrice(),
                command.stockQuantity(),
                command.taxRate());

        writeProductRepository.save(updatedProduct);
        readProductRepository.save(ProductDTO.fromProduct(updatedProduct));
    }
}
