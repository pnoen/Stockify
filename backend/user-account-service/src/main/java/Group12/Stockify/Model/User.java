package Group12.Stockify;

public class User {

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String business;

    public User() {}

    public User(String firstName, String lastName, String email, String password, String business) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.business = business;
    }

    // Getter for first name
    public String getFirstName() {
        return firstName;
    }

    // Setter for first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter for last name
    public String getLastName() {
        return lastName;
    }

    // Setter for last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for business
    public String getBusiness() {
        return business;
    }

    // Setter for business
    public void setBusiness(String business) {
        this.business = business;
    }
}