package app.mappers;

import app.dtos.UserDTO;
import app.model.User;

public class UserMapper {
	public static UserDTO toDto(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO(
        		user.getId(), 
        		user.getFirstName(),
        		user.getLastName(),
        		user.getEmail());

        return dto;
    }
}
