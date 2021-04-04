package uz.qirol.eombor.contents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.qirol.eombor.model.ReqAccepted;
import uz.qirol.eombor.model.User;
import uz.qirol.eombor.repository.AcceptedRepository;
import uz.qirol.eombor.repository.UserRepository;

import java.security.Principal;

@RestController
@Controller
@RequestMapping(value = "/types")
public class Types {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcceptedRepository acceptedRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTypes(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (!acceptedRepository.findByUserId(user.getId()).isPresent()){
            return ResponseEntity.ok("user");
        }

        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);

    }
}
