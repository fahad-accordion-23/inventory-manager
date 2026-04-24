package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

public class RemoveProductCommandHandler {
    private final IProductWriteRepository writeProductRepository;
    private final IProductReadRepository readProductRepository;

    public RemoveProductCommandHandler(IProductWriteRepository writeProductRepository,
            IProductReadRepository readProductRepository) {
        this.writeProductRepository = writeProductRepository;
        this.readProductRepository = readProductRepository;
    }

    @CommandHandler
    public void handleProductRemoved(RemoveProductCommand command) {
        writeProductRepository.delete(command.productId());
        readProductRepository.delete(command.productId());
    }
}
