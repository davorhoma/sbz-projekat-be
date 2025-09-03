package app.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.AuthResponseDTO;
import app.dtos.UserDTO;
import app.mappers.UserMapper;
import app.model.Place;
import app.model.User;
import app.model.UserRole;
import app.repositories.UserRepository;
import app.utils.JwtUtil;

@Service
public class AuthService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public User register(String firstName, String lastName, String email, String password, UUID placeId) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
        	throw new Exception("User already exists");
        }

        try {
        	User user = new User(firstName, lastName, email, password, placeService.findById(placeId), UserRole.USER);
        	
        	return userRepository.save(user);
        	
        } catch (Exception e) {
        	throw new Exception("Place does not exists");
        }
    }
	
	public AuthResponseDTO login(String email, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new Exception("Invalid email or password");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid email or password");
        }
        
        String token = jwtUtil.generateToken(user.getId(), email, user.getRole().name());

        return new AuthResponseDTO(token);
    }
}
