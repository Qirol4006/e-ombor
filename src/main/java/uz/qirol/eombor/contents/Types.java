package uz.qirol.eombor.contents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.qirol.eombor.model.ProductType;
import uz.qirol.eombor.model.ReqAccepted;
import uz.qirol.eombor.model.User;
import uz.qirol.eombor.repository.AcceptedRepository;
import uz.qirol.eombor.repository.TypesRepository;
import uz.qirol.eombor.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@RestController
@Controller
@RequestMapping(value = "/types")
public class Types {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcceptedRepository acceptedRepository;
    @Autowired
    private TypesRepository typesRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTypes(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()) {
            return ResponseEntity.ok("user");
        }
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()) {
            return ResponseEntity.ok("notAccepted");
        }
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.ok(typesRepository.findAllByMarketId(reqAccepted.getMarketId()));
    }


    @GetMapping(value = "/all/{id}")
    public ResponseEntity<?> getAllTypesByParentId(@PathVariable("id") Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()) {
            return ResponseEntity.ok("user");
        }
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()) {
            return ResponseEntity.ok("notAccepted");
        }
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);

        List<ProductType> productType = typesRepository
                .findAllByMarketIdAndParentId(reqAccepted.getMarketId(), id);
        return ResponseEntity.ok(productType);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveTypes(@RequestBody ProductType productType, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (!acceptedRepository.findByUserId(user.getId()).isPresent()) {
            return ResponseEntity.ok("notAccepted");
        }
        ReqAccepted reqAccepted = acceptedRepository.findByUserId(user.getId()).orElse(null);
        if (!reqAccepted.getMarketId().equals(productType.getMarketId())){
            return ResponseEntity.ok("market");
        }
        return ResponseEntity.ok(typesRepository.save(productType));
    }
}