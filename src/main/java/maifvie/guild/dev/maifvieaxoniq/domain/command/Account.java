package maifvie.guild.dev.maifvieaxoniq.domain.command;

import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.IllegalStateAmountException;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.OverdrawnAccountException;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.CreateAccountCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.DepositMoneyCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.WithdrawMoneyCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.AccountCreatedEvent;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.MoneyDepositedEvent;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.events.MoneyWithdrawEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Aggregate
public class Account {

    @AggregateIdentifier
    private UUID accountId;
    private BigDecimal balance;

    public Account() {
    }

    @CommandHandler
    public Account(CreateAccountCommand command) {
        Optional.ofNullable(command.getBalance())
                .map(startingBalance -> AggregateLifecycle.apply(new AccountCreatedEvent(UUID.randomUUID(), startingBalance)))
                .orElseGet(() -> AggregateLifecycle.apply(new AccountCreatedEvent(UUID.randomUUID(), BigDecimal.ZERO)));
    }

    @CommandHandler
    public void handle(DepositMoneyCommand command) throws IllegalStateAmountException {
        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateAmountException();
        }
        AggregateLifecycle.apply(new MoneyDepositedEvent(accountId, command.getAmount()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) throws IllegalStateAmountException, OverdrawnAccountException {
        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateAmountException();
        }
        if (balance.compareTo(command.getAmount()) < 0) {
            throw new OverdrawnAccountException();
        }
        AggregateLifecycle.apply(new MoneyWithdrawEvent(accountId, command.getAmount()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        accountId = event.getAccountId();
        balance = event.getBalance();
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent event) {
        balance = balance.add(event.getAmount());
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawEvent event) {
        balance = balance.subtract(event.getAmount());
    }

}
