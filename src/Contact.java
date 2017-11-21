import javafx.beans.property.SimpleStringProperty;
import java.util.regex.*;
import java.io.IOException;

public class Contact{

    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty address;
    private SimpleStringProperty birthday;
    private SimpleStringProperty notes;

    Contact(){

    }

    Contact( String firstName, String lastName, String email, String phoneNumber, String address, String birthday, String notes ){
        this.firstName = new SimpleStringProperty( firstName );
        this.lastName = new SimpleStringProperty( lastName );
        this.email = new SimpleStringProperty( email );
        this.phoneNumber = new SimpleStringProperty( phoneNumber );
        this.address = new SimpleStringProperty( address );
        this.birthday = new SimpleStringProperty( birthday );
        this.notes = new SimpleStringProperty( notes );
    }

    public String getFirstName() { return this.firstName.get(); }
    public SimpleStringProperty firstNameProperty() { return firstName; }
    public void setFirstName(String firstName) { this.firstName.set( firstName ); }

    public String getLastName() { return this.lastName.get(); }
    public SimpleStringProperty lastNameProperty() { return lastName; }
    public void setLastName(String lastName) { this.lastName.set( lastName ); }

    public String getEmail() { return email.get(); }
    public SimpleStringProperty emailProperty() { return email; }
    public void setEmail(String email) throws InvalidEmailException{
        if( Pattern.compile("\\S+@\\S+\\.\\S+").matcher( email ).matches() )
            this.email.set( email );
        else throw new InvalidEmailException();
    }

    public String getPhoneNumber() { return phoneNumber.get(); }
    public SimpleStringProperty phoneNumberProperty() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException{
        if( Pattern.compile("\\d{4,}").matcher( phoneNumber ).matches() )
            this.phoneNumber.set( phoneNumber );
        else throw new InvalidPhoneNumberException();
    }

    public String getAddress() { return address.get(); }
    public SimpleStringProperty addressProperty() { return address; }
    public void setAddress(String address) { this.address.set( address ); }

    public String getBirthday() { return birthday.get(); }
    public SimpleStringProperty birthdayProperty() { return birthday; }
    public void setBirthday(String birthday) { this.birthday.set( birthday ); }

    public String getNotes() { return notes.get(); }
    public SimpleStringProperty notesProperty() { return notes; }
    public void setNotes(String notes) { this.notes.set( notes ); }
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
