package app.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.User;
import app.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User findById(UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new Exception("User does not exist");
		}
		
		return user.get();
	}
}
