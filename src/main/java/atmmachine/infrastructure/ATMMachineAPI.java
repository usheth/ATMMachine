package atmmachine.infrastructure;

import atmmachine.application.ATMMachine;
import atmmachine.common.ATMMachineConstants;
import atmmachine.common.TypeConverter;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.AuthenticationRequest;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.TransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atmmachine")
public class ATMMachineAPI {

  private static final String DEFAULT_TOKEN_VALUE = "";
  private static final String DEFAULT_ACCOUNT_ID_VALUE = "-1";
  private static final String ACCOUNT_ID_PARAM = "accountId";
  private static final String TOKEN_PARAM = "token";

  @Autowired
  private ATMMachine atmMachine;

  @GetMapping
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    return "Hello, " + name;
  }

  @PostMapping
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
    AuthenticationResult authenticationResult = atmMachine
        .verifyCredentials(authenticationRequest.getCard(), authenticationRequest.getPin());
    HttpStatus status =
        StringUtils.isEmpty(authenticationResult.getToken()) ? HttpStatus.UNAUTHORIZED
            : HttpStatus.OK;
    return ResponseEntity.status(status).body(authenticationResult);
  }

  @PutMapping
  @RequestMapping(value = "/deposit", method = RequestMethod.PUT)
  public ResponseEntity deposit(
      @RequestParam(value = TOKEN_PARAM, defaultValue = DEFAULT_TOKEN_VALUE) String token,
      @RequestParam(value = ACCOUNT_ID_PARAM, defaultValue = DEFAULT_ACCOUNT_ID_VALUE) String accountId,
      @RequestBody Money money) {
    Long accountIdLong = TypeConverter.stringToLong(accountId);
    if (accountIdLong == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TransactionResult result = atmMachine.depositMoney(token, accountIdLong, money);
    if (result.didTransactionSucceed()) {
      return ResponseEntity.status(HttpStatus.OK).body(result);
    } else if (result.getMessage().equals(ATMMachineConstants.INVALID_TOKEN)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
  }

  @PutMapping
  @RequestMapping(value = "/withdraw", method = RequestMethod.PUT)
  public ResponseEntity withdraw(
      @RequestParam(value = TOKEN_PARAM, defaultValue = DEFAULT_TOKEN_VALUE) String token,
      @RequestParam(value = ACCOUNT_ID_PARAM, defaultValue = DEFAULT_ACCOUNT_ID_VALUE) String accountId,
      @RequestBody Amount amount) {
    Long accountIdLong = TypeConverter.stringToLong(accountId);
    if (accountIdLong == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TransactionResult result = atmMachine.withdrawMoney(token, accountIdLong, amount);
    if (result.didTransactionSucceed()) {
      return ResponseEntity.status(HttpStatus.OK).body(result);
    } else if (result.getMessage().equals(ATMMachineConstants.INVALID_TOKEN)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
  }

  @GetMapping
  @RequestMapping(value = "/balance", method = RequestMethod.GET)
  public ResponseEntity getBalance(
      @RequestParam(value = TOKEN_PARAM, defaultValue = DEFAULT_TOKEN_VALUE) String token,
      @RequestParam(value = ACCOUNT_ID_PARAM, defaultValue = DEFAULT_ACCOUNT_ID_VALUE) String accountId) {
    Long accountIdLong = TypeConverter.stringToLong(accountId);
    if (accountIdLong == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(atmMachine.getAccountBalance(token, accountIdLong));
  }

  @PostMapping
  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ResponseEntity logout(
      @RequestParam(value = TOKEN_PARAM, defaultValue = DEFAULT_TOKEN_VALUE) String token,
      @RequestParam(value = ACCOUNT_ID_PARAM, defaultValue = DEFAULT_ACCOUNT_ID_VALUE) String accountId) {
    Long accountIdLong = TypeConverter.stringToLong(accountId);
    if (accountIdLong == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(atmMachine.logout(token, accountIdLong));
  }

}
