package maifvie.guild.dev.maifvieaxoniq.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationRequest {
    private OperationType type;
    private BigDecimal amount;
}
