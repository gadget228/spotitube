package data.mappers;

import data.domain.User;
import dto.login.LoginResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class UserMapper implements IMapper<User, LoginResponseDTO>
{
    private static UserMapper mapper;

    @Override
    public User convertToEntity(LoginResponseDTO dto, Object... args)
    {
        User user = new User();
        user.setName(dto.getUser());
        user.setToken(dto.getToken());

        return user;
    }

    @Override
    public LoginResponseDTO convertToDTO(User entity, Object... args)
    {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setToken(entity.getToken());
        dto.setUser(entity.getName());

        return dto;
    }

    @Override
    public List<User> convertToEntity(List<LoginResponseDTO> dtos, Object... args)
    {
        List<User> users = new ArrayList<>();

        for (LoginResponseDTO dto : dtos) {
            users.add(convertToEntity(dto, args));
        }

        return users;
    }

    @Override
    public List<LoginResponseDTO> convertToDTO(List<User> entities, Object... args)
    {
        List<LoginResponseDTO> dtos = new ArrayList<>();

        for (User user : entities) {
            dtos.add(convertToDTO(user, args));
        }

        return dtos;
    }

    public static UserMapper getInstance()
    {
        if (mapper == null) {
            mapper = new UserMapper();
        }

        return mapper;
    }
}
