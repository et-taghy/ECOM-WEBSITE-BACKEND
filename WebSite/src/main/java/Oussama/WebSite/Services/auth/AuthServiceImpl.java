package Oussama.WebSite.Services.auth;

import Oussama.WebSite.Entity.Enum.UserRole;
import Oussama.WebSite.Entity.User;
import Oussama.WebSite.Repository.UserRepository;
import Oussama.WebSite.dto.SignUpRequest;
import Oussama.WebSite.dto.UserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
@Autowired
    private UserRepository userRepository;
@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDto createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        user.setRole(UserRole.User);
        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
    @PostConstruct

    public void createAdminAccount() {
        User adminAccount =userRepository.findByRole(UserRole.ADMIN);
        if(null==adminAccount){
            User user =new User();
            user.setEmail("adminElectro@gmail.com");
            user.setName("Oussama");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin2002"));
            userRepository.save(user);
        }
    }


}
