package maifvie.guild.dev.maifvieaxoniq.infrastructure;

import maifvie.guild.dev.maifvieaxoniq.domain.query.AccountBalanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountBalanceViewRepository extends JpaRepository<AccountBalanceView, UUID> {
}
