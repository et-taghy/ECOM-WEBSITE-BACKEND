package Oussama.WebSite.Services.auth;

import Oussama.WebSite.dto.SignUpRequest;
import Oussama.WebSite.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignUpRequest signUpRequest);

    boolean hasUserWithEmail(String email);
}
