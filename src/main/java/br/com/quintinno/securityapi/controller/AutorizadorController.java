package br.com.quintinno.securityapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;
import br.com.quintinno.securityapi.service.TokenService;
import br.com.quintinno.securityapi.transfer.LoginRequestTransfer;
import br.com.quintinno.securityapi.transfer.ResponseTransfer;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;
import br.com.quintinno.securityapi.utility.MensagemUtility;

@RestController
@RequestMapping("/autorizador")
public class AutorizadorController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signin")
    public ResponseEntity login(@RequestBody LoginRequestTransfer loginRequestTransfer) {
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByIdentificador(loginRequestTransfer.identificador())
                .orElseThrow(() -> new RuntimeException(MensagemUtility.MENSAGEM_ERROR_01));
        if(passwordEncoder.matches(loginRequestTransfer.senha(), usuarioEntity.getSenha())) {
            String token = this.tokenService.generateToken(usuarioEntity);
            return ResponseEntity.ok(new ResponseTransfer(usuarioEntity.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody SignupRequestTransfer signupRequestTransfer) {
        Optional<UsuarioEntity> usuarioEntity = this.usuarioRepository.findByIdentificador(signupRequestTransfer.identificador());
        if(usuarioEntity.isEmpty()) {
            UsuarioEntity usuarioEntityNovo = new UsuarioEntity();
            usuarioEntityNovo.setSenha(passwordEncoder.encode(signupRequestTransfer.senha()));
            usuarioEntityNovo.setIdentificador(signupRequestTransfer.identificador());
            usuarioEntityNovo.setNome(signupRequestTransfer.nome());
            this.usuarioRepository.save(usuarioEntityNovo);
            String token = this.tokenService.generateToken(usuarioEntityNovo);
            return ResponseEntity.ok(new ResponseTransfer(usuarioEntityNovo.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

}
