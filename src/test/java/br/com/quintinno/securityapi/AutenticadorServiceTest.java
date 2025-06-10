package br.com.quintinno.securityapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;
import br.com.quintinno.securityapi.service.AutenticadorService;
import br.com.quintinno.securityapi.service.TokenService;
import br.com.quintinno.securityapi.transfer.ResponseTransfer;
import br.com.quintinno.securityapi.transfer.SigninRequestTransfer;
import br.com.quintinno.securityapi.transfer.SignupRequestTransfer;
import br.com.quintinno.securityapi.utility.MensagemUtility;

@ExtendWith(MockitoExtension.class)
public class AutenticadorServiceTest {

    @InjectMocks
    private AutenticadorService autenticadorService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cadastrarNovoUsuarioDoSistemaComSucesso() {

        String senhaEncoder = "senha-encoder";

        String tokenGerado = "token-gerado";

        SignupRequestTransfer signupRequestTransfer = new SignupRequestTransfer(
                "liwaias.lurzi@email.com",
                "liwaiasusronkountolurzi",
                "Liwaias Usron Kount Olurzi");

        when(this.usuarioRepository.findByIdentificador(signupRequestTransfer.identificador()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(signupRequestTransfer.senha())).thenReturn(senhaEncoder);
        when(this.tokenService.generateToken(any(UsuarioEntity.class))).thenReturn(tokenGerado);

        ResponseTransfer responseTransfer = this.autenticadorService.signup(signupRequestTransfer);

        assertNotNull(responseTransfer);
        assertEquals("Liwaias Usron Kount Olurzi", responseTransfer.nome());
        assertEquals(tokenGerado, responseTransfer.token());
    }

    @Test
    public void deveRetonarTokenQuandoCredenciaisValidas() {

        String tokenGerado = "token-gerado";

        UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setNome("Liwaias Usron Kount Olurzi");
            usuarioEntity.setIdentificador("liwaias.lurzi@email.com");
            usuarioEntity.setSenha("liwaiasusronkountolurzi");

        SigninRequestTransfer signinRequestTransfer = new SigninRequestTransfer(
            usuarioEntity.getIdentificador(),
            usuarioEntity.getSenha()
        );

        when(this.usuarioRepository.findByIdentificador(usuarioEntity.getIdentificador())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(signinRequestTransfer.senha(), usuarioEntity.getSenha())).thenReturn(true);
        when(this.tokenService.generateToken(usuarioEntity)).thenReturn(tokenGerado);

        ResponseTransfer responseTransfer = this.autenticadorService.signin(signinRequestTransfer);

        assertNotNull(responseTransfer);
        assertEquals("Liwaias Usron Kount Olurzi", responseTransfer.nome());
        assertEquals(tokenGerado, responseTransfer.token());

    }

    @Test
    public void deveRetonarNomeDoUsuarioQuandoCredenciaisForemValidas() {

        String tokenGerado = "token-gerado";

        UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setNome("Liwaias Usron Kount Olurzi");
            usuarioEntity.setIdentificador("liwaias.lurzi@email.com");
            usuarioEntity.setSenha("liwaiasusronkountolurzi");

        SigninRequestTransfer signinRequestTransfer = new SigninRequestTransfer(
            usuarioEntity.getIdentificador(),
            usuarioEntity.getSenha()
        );

        when(this.usuarioRepository.findByIdentificador(usuarioEntity.getIdentificador())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(signinRequestTransfer.senha(), usuarioEntity.getSenha())).thenReturn(true);
        when(this.tokenService.generateToken(usuarioEntity)).thenReturn(tokenGerado);

        ResponseTransfer responseTransfer = this.autenticadorService.signin(signinRequestTransfer);

        assertEquals(usuarioEntity.getNome(), responseTransfer.nome());

    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setNome("Xaebor Untwa Xofou Fimil Folulon");
            usuarioEntity.setIdentificador("xaebor.folulon@email.com");
            usuarioEntity.setSenha("xaeboruntwaxofoufimilfolulon");

        SigninRequestTransfer signinRequestTransfer = new SigninRequestTransfer(
            usuarioEntity.getIdentificador(),
            usuarioEntity.getSenha()
        );

        when(this.usuarioRepository.findByIdentificador(usuarioEntity.getIdentificador())).thenReturn(Optional.of(usuarioEntity));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            this.autenticadorService.signin(signinRequestTransfer);
        });

        assertEquals(MensagemUtility.MENSAGEM_ERROR_01, runtimeException.getMessage());
        
    }

    @Test
    public void deveLancarExcecaoQuandoSenhaForInvalida() {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setNome("Xaebor Untwa Xofou Fimil Folulon");
            usuarioEntity.setIdentificador("xaebor.folulon@email.com");
            usuarioEntity.setSenha("xaeboruntwaxofoufimilfolulon");

        SigninRequestTransfer signinRequestTransfer = new SigninRequestTransfer(
            usuarioEntity.getIdentificador(),
            "SENHA_INVALIDA"
        );

        when(this.usuarioRepository.findByIdentificador(usuarioEntity.getIdentificador())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(signinRequestTransfer.senha(), usuarioEntity.getSenha())).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            this.autenticadorService.signin(signinRequestTransfer);
        });

        assertEquals(MensagemUtility.MENSAGEM_ERROR_01, runtimeException.getMessage());

    }

    /**
     * O Identificador, segundo a Especificação Técnica, pode ser: E-mail, Telefone ou CPF/CNPJ
     * Essa forma de Identidicador é parametrizada pelo Sistema responsável pelo cadastro de usuários
     */
    // FIXME -- Criar Anotação Persinalizada para Validar o Tipo de Identificador
    public void deveLancarExcecaoQuandoIdentificadorForInvalido() { }

}
