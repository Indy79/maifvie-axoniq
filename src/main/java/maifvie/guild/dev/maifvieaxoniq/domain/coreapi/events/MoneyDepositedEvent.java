package maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MoneyDepositedEvent {
    private UUID accountId;
    private BigDecimal amount;
}
