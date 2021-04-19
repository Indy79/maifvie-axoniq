package maifvie.guild.dev.maifvieaxoniq.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    private BigDecimal startingBalance;
}
