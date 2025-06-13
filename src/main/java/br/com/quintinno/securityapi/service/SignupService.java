package br.com.quintinno.securityapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;
import br.com.quintinno.securityapi.transfer.ResponseTransfer;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;
import br.com.quintinno.securityapi.utility.MensagemUtility;

@Service
public class SignupService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public ResponseTransfer signup(SignupRequestTransfer signupRequestTransfer) {
        Optional<UsuarioEntity> usuarioEntity = this.usuarioRepository.findByIdentificador(signupRequestTransfer.identificador());
        if (!usuarioEntity.isEmpty()) {
            throw new RuntimeException(MensagemUtility.MENSAGEM_ALERTA_01);
        }
        UsuarioEntity usuarioEntityNovo = new UsuarioEntity();
            usuarioEntityNovo.setSenha(passwordEncoder.encode(signupRequestTransfer.senha()));
            usuarioEntityNovo.setIdentificador(signupRequestTransfer.identificador());
            usuarioEntityNovo.setNome(signupRequestTransfer.nome());
            usuarioEntityNovo.setDataNascimento(signupRequestTransfer.dataNascimento());
            usuarioEntityNovo.setTelefone(signupRequestTransfer.telefone());
            this.usuarioRepository.save(usuarioEntityNovo);
        String token = this.tokenService.generateToken(usuarioEntityNovo);
        return new ResponseTransfer(usuarioEntityNovo.getNome(), token);
    }

}
