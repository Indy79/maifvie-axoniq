package maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WithdrawMoneyCommand {
    @TargetAggregateIdentifier private UUID accountId;
    private BigDecimal amount;
}
