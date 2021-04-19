package maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.RoutingKey;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateAccountCommand {
    @RoutingKey
    private UUID foodCartId;

    private BigDecimal balance;

    public CreateAccountCommand(BigDecimal balance) {
        this.balance = balance;
    }
}
