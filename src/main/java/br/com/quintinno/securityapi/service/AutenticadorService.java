package br.com.quintinno.securityapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;
import br.com.quintinno.securityapi.transfer.SigninRequestTransfer;
import br.com.quintinno.securityapi.transfer.ResponseTransfer;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;
import br.com.quintinno.securityapi.utility.MensagemUtility;

@Service
public class AutenticadorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public ResponseTransfer signin(SigninRequestTransfer signinRequestTransfer) {
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByIdentificador(signinRequestTransfer.identificador())
                .orElseThrow(() -> new RuntimeException(MensagemUtility.MENSAGEM_ERROR_01));
        if (!passwordEncoder.matches(signinRequestTransfer.senha(), usuarioEntity.getSenha())) {
            throw new RuntimeException(MensagemUtility.MENSAGEM_ERROR_01);
        }
        return new ResponseTransfer(usuarioEntity.getNome(), this.tokenService.generateToken(usuarioEntity));
    }

    public ResponseTransfer signup(SignupRequestTransfer signupRequestTransfer) {
        Optional<UsuarioEntity> usuarioEntity = this.usuarioRepository.findByIdentificador(signupRequestTransfer.identificador());
        if (!usuarioEntity.isEmpty()) {
            throw new RuntimeException(MensagemUtility.MENSAGEM_ALERTA_01);
        }
        UsuarioEntity usuarioEntityNovo = new UsuarioEntity();
            usuarioEntityNovo.setSenha(passwordEncoder.encode(signupRequestTransfer.senha()));
            usuarioEntityNovo.setIdentificador(signupRequestTransfer.identificador());
            usuarioEntityNovo.setNome(signupRequestTransfer.nome());
            this.usuarioRepository.save(usuarioEntityNovo);
        String token = this.tokenService.generateToken(usuarioEntityNovo);
        return new ResponseTransfer(usuarioEntityNovo.getNome(), token);
    }

}
