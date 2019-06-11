package atmmachine.infrastructure;

import atmmachine.application.ATMMachine;
import atmmachine.common.ATMMachineConstants;
import atmmachine.common.TypeConverter;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.entities.AuthenticationRequest;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.exception.AuthenticationServiceException;
import atmmachine.domain.model.transaction.TransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atmmachine")
public class ATMMachineAPI {

  @Autowired
  ATMMachine atmMachine;

  @GetMapping
  @RequestMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    return "Hello, " + name;
  }

  @PostMapping
  @RequestMapping("/authenticate")
  public ResponseEntity authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
    AuthenticationResult authenticationResult = atmMachine
        .verifyCredentials(authenticationRequest.getCard(), authenticationRequest.getPin());
    HttpStatus status =
        StringUtils.isEmpty(authenticationResult.getToken()) ? HttpStatus.UNAUTHORIZED
            : HttpStatus.OK;
    return ResponseEntity.status(status).body(authenticationResult);
  }

  @PostMapping
  @RequestMapping("/deposit")
  public ResponseEntity deposit(@RequestParam(value = "token", defaultValue = "World") String token,
      @RequestParam(value = "accountId", defaultValue = "1") String accountId,
      @RequestBody Money money) {
    Long accountIdLong = TypeConverter.stringToLong(accountId);
    if (accountIdLong == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    try {
      TransactionResult result = atmMachine.depositMoney(token, accountIdLong, money);
      if (result.didTransactionSucceed()) {
        return ResponseEntity.status(HttpStatus.OK).body(result);
      } else if (result.getMessage().equals(ATMMachineConstants.INVALID_TOKEN)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    } catch (AuthenticationServiceException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
