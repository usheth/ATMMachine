package atmmachine.domain.model;

import atmmachine.application.ATMMachine;
import atmmachine.infrastructure.CashDispenser;
import atmmachine.infrastructure.DefaultAuthenticationService;
import atmmachine.infrastructure.DefaultTransactionRecorder;
import atmmachine.infrastructure.DefaultTransactionService;
import atmmachine.infrastructure.LocalSQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtmMachineRestConfiguration {

  @Autowired
  private ATMMachineRepository atmMachineRepository;
  @Autowired
  private AuthenticationService authenticationService;
  @Autowired
  private TransactionService transactionService;
  @Autowired
  private TransactionRecorder transactionRecorder;
  @Autowired
  private CashDispenser cashDispenser;
  @Autowired
  private ATMMachine atmMachine;

  @Bean
  ATMMachine getAtmMachine() {
    return new ATMMachine(authenticationService, transactionService, transactionRecorder,
        cashDispenser);
  }

  @Bean
  CashDispenser getCashDispenser() {
    return new CashDispenser();
  }

  @Bean
  TransactionRecorder getTransactionRecorder() {
    return new DefaultTransactionRecorder();
  }

  @Bean
  TransactionService getTransactionService() {
    return new DefaultTransactionService();
  }

  @Bean
  AuthenticationService getAuthenticationService() {
    return new DefaultAuthenticationService(atmMachineRepository);
  }

  @Bean
  ATMMachineRepository getAtmMachineRepository() {
    return new LocalSQLRepository();
  }

}
