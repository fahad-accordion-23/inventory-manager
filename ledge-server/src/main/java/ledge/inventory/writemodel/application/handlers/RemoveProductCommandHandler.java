package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

import org.springframework.stereotype.Service;

@Service
public class RemoveProductCommandHandler implements CommandHandler<RemoveProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final IProductReadRepository readProductRepository;

    public RemoveProductCommandHandler(IProductWriteRepository writeProductRepository,
            IProductReadRepository readProductRepository) {
        this.writeProductRepository = writeProductRepository;
        this.readProductRepository = readProductRepository;
    }

    @Override
    public void handle(RemoveProductCommand command) {
        writeProductRepository.delete(command.productId());
        readProductRepository.delete(command.productId());
    }
}
