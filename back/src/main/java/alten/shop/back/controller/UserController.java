package alten.shop.back.controller;

import alten.shop.back.dto.UserDTO;
import alten.shop.back.entity.UserEntity;
import alten.shop.back.repository.UserRepository;
import alten.shop.back.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @PostMapping()
    public ResponseEntity<?> createUsers(@RequestBody UserDTO user){

        UserEntity newUser = userRepository.findUserByEmail(user.getEmail());

        if(newUser == null){

            var userC = UserEntity.builder()
                    .firstname(user.getFirstname())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();
            var savedUser = userRepository.save(userC);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(Constantes.USER_ALLREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }
}
