package maifvie.guild.dev.maifvieaxoniq.application;

import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.CreateAccountCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.DepositMoneyCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.coreapi.commands.WithdrawMoneyCommand;
import maifvie.guild.dev.maifvieaxoniq.domain.query.AccountBalanceView;
import maifvie.guild.dev.maifvieaxoniq.domain.query.FindAccountQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class AccountController {

    private final QueryGateway gatewayQuery;
    private final CommandGateway commandGateway;

    public AccountController(QueryGateway gatewayQuery, CommandGateway commandGateway) {
        this.gatewayQuery = gatewayQuery;
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public CompletableFuture<AccountBalanceView> handle(@RequestBody AccountCreationRequest accountCreationRequest) {
        return Optional.ofNullable(accountCreationRequest.getStartingBalance())
                .map(balance -> commandGateway.send(new CreateAccountCommand(balance)).thenApply(o -> {
                    if (o instanceof UUID) {
                        return AccountBalanceView.builder().accountId((UUID) o).build();
                    }
                    throw new IllegalStateException();
                }))
                .orElseGet(() -> commandGateway.send(new CreateAccountCommand()).thenApply(o -> {
                    if (o instanceof UUID) {
                        return AccountBalanceView.builder().accountId((UUID) o).build();
                    }
                    throw new IllegalStateException();
                }));
    }

    @PostMapping("/accounts/{accountId}/operations")
    public void handle(@PathVariable UUID accountId, @RequestBody AccountOperationRequest accountOperationRequest) {
        switch (accountOperationRequest.getType()) {
            case DEPOSIT: {
                commandGateway.send(new DepositMoneyCommand(accountId, accountOperationRequest.getAmount()));
                break;
            }
            case WITHDRAW: {
                commandGateway.send(new WithdrawMoneyCommand(accountId, accountOperationRequest.getAmount()));
                break;
            }
            default: {
                throw new IllegalArgumentException("Type not recognize");
            }
        }
    }

    @GetMapping("accounts/{accountId}")
    public CompletableFuture<AccountBalanceView> handle(@PathVariable UUID accountId) {
        return gatewayQuery.query(
                new FindAccountQuery(accountId),
                ResponseTypes.instanceOf(AccountBalanceView.class)
        );
    }

}
