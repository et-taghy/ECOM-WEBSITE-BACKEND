package Oussama.WebSite.Controller;

import Oussama.WebSite.Entity.User;
import Oussama.WebSite.Repository.UserRepository;
import Oussama.WebSite.Services.auth.AuthService;
import Oussama.WebSite.Utils.JwtUtil;
import Oussama.WebSite.dto.AuthenticationRequest;
import Oussama.WebSite.dto.SignUpRequest;
import Oussama.WebSite.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    private final AuthService authService;
    @PostMapping("/Login")
    private void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        try {

            System.out.println("Avant Authentication");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            System.out.println("apres l'Authentication");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou le password est incorrect.");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );
            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers","Authorization,X-PINGOTHER, Origin, "
            +"X-Requested-With, Content-Type, Accept,X-Custom-header");
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }
    @PostMapping("/Sign-Up")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signUpRequest){
if(authService.hasUserWithEmail(signUpRequest.getEmail())){
    return new ResponseEntity<>("ce utilisateur est deja existe", HttpStatus.NOT_ACCEPTABLE);
}
        UserDto userDto=authService.createUser(signUpRequest);
return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

}
