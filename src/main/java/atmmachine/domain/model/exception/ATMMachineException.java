package atmmachine.domain.model.exception;

public class ATMMachineException extends Exception {

    private String message;

    public ATMMachineException(String message) {
        //super();
        this.message = message;
    }

}
