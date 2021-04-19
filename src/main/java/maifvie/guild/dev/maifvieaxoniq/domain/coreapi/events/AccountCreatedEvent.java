package maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountCreatedEvent {
    private UUID accountId;
    private BigDecimal balance;
}
