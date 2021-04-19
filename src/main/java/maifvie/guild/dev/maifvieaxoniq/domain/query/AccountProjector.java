package maifvie.guild.dev.maifvieaxoniq.domain.query;

import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.AccountNotFoundException;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.AccountCreatedEvent;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.MoneyDepositedEvent;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.MoneyWithdrawEvent;
import maifvie.guild.dev.maifvieaxoniq.infrastructure.AccountBalanceViewRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProjector {

    private final AccountBalanceViewRepository accountBalanceViewRepository;

    @Autowired
    public AccountProjector(AccountBalanceViewRepository accountBalanceViewRepository) {
        this.accountBalanceViewRepository = accountBalanceViewRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        AccountBalanceView accountBalanceView = new AccountBalanceView(event.getAccountId(), BigDecimal.ZERO);
        accountBalanceViewRepository.save(accountBalanceView);
    }
    @EventHandler
    public void on(MoneyDepositedEvent event) {
        accountBalanceViewRepository.findById(event.getAccountId())
                .map(accountBalanceView -> new AccountBalanceView(event.getAccountId(), accountBalanceView.getBalance().add(event.getAmount())))
                .map(accountBalanceViewRepository::save);
    }
    @EventHandler
    public void on(MoneyWithdrawEvent event) {
        accountBalanceViewRepository.findById(event.getAccountId())
                .map(accountBalanceView -> new AccountBalanceView(event.getAccountId(), accountBalanceView.getBalance().subtract(event.getAmount())))
                .map(accountBalanceViewRepository::save);
    }

    @QueryHandler
    public AccountBalanceView handle(FindAccountQuery query) {
        return accountBalanceViewRepository.findById(query.getAccountId())
                .orElseThrow(AccountNotFoundException::new);
    }
}
