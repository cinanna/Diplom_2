package site.nomoreparties.stellarburgers.requests;

public class UserDataToCreate {
    private String email;
    private String password;
    private String name;

    public UserDataToCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public UserDataToCreate() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
