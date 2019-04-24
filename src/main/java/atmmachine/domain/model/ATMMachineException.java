package atmmachine.domain.model;

public class ATMMachineException extends Exception {

    private String message;

    public ATMMachineException(String message) {
        //super();
        this.message = message;
    }

}
