package maifvie.guild.dev.maifvieaxoniq.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceView {
    @Id private UUID accountId;
    private BigDecimal balance;
}
