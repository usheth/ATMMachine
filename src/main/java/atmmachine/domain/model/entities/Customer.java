package atmmachine.domain.model.entities;

import java.util.Date;

public class Customer {

    private String type = "Customer";
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String emailId;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    //should be an enum
    private String state;
    private String zipCode;

}
