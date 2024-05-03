package ac.rs.metropolitan.anteaprimorac5157.entity;

public class RegistrationCommand {

    private String username;
    private String password;
    private String repeatPassword;

    public RegistrationCommand() {
    }

    public RegistrationCommand(String username, String password, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUsername() {
        return username;
    }

    public RegistrationCommand setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegistrationCommand setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public RegistrationCommand setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
        return this;
    }
}
