package by.hurynovich.mus_overview.security.jwt;

public class AccountCredentials {

    private String username;
    private String password;

    String getUsername() { return username; }
    String getPassword() { return password; }

    public void setUsername(final String _username) { this.username = _username; }
    public void setPassword(final String _password) { this.password = _password; }
}
