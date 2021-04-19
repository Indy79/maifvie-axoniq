package maifvie.guild.dev.maifvieaxoniq.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountQuery {
    private UUID accountId;
}
