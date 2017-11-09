import java.util.regex.*;
import java.io.IOException;

public class Contact {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String birthday;
    private String notes;

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) throws InvalidEmailException{
        if( Pattern.compile("\\S+@\\S+\\.\\S+").matcher( email ).matches() )
            this.email = email;
        else throw new InvalidEmailException();
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException{
        if( Pattern.compile("\\d{4,}").matcher( phoneNumber ).matches() )
            this.phoneNumber = phoneNumber;
        else throw new InvalidPhoneNumberException();
    }

    public void setAddress(String address) { this.address = address; }
}

class InvalidEmailException extends IOException{

    public InvalidEmailException(){
        super("Error: email must  contain one \'@\' and one \'.\'");
    }
}
class InvalidPhoneNumberException extends IOException{
    public InvalidPhoneNumberException(){
        super("Error: number must have only digits and a minimum of four");
    }
}
