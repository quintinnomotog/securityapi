package br.com.quintinno.securityapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;
import br.com.quintinno.securityapi.transfer.ResponseTransfer;
import br.com.quintinno.securityapi.transfer.SigninRequestTransfer;
import br.com.quintinno.securityapi.utility.MensagemUtility;

@Service
public class SigninService {

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

}
