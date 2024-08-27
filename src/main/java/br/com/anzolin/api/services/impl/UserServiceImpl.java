package br.com.anzolin.api.services.impl;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.repositories.UserRepository;
import br.com.anzolin.api.services.UserService;
import br.com.anzolin.api.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }
}
