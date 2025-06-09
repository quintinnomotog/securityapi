package br.com.quintinno.securityapi.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.quintinno.securityapi.entity.UsuarioEntity;
import br.com.quintinno.securityapi.repository.UsuarioRepository;

@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdentificador(identificador)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado!"));
        return new org.springframework.security.core.userdetails.User(usuarioEntity.getIdentificador(),
                usuarioEntity.getSenha(), new ArrayList<>());
    }

}
