package dto.login;

public record LoginRequestDTO(String user, String password)
{
    public LoginRequestDTO(String user, String password)
    {
        this.user = user;
        this.password = password;
    }
}
