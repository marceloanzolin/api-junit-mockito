package br.com.anzolin.api.services.impl;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.domain.dto.UserDTO;
import br.com.anzolin.api.repositories.UserRepository;
import br.com.anzolin.api.services.UserService;
import br.com.anzolin.api.services.exceptions.DataIntegrationViolationException;
import br.com.anzolin.api.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(UserDTO userDto) {
        this.findByEmail(userDto);
        User user = mapper.map(userDto, User.class);
        return userRepository.save(user);
    }

    @Override
    public User update(UserDTO userDto) {
        this.findByEmail(userDto);
        return userRepository.save(mapper.map(userDto, User.class));
    }

    @Override
    public void delete(Integer id) {
        this.findById(id);
         userRepository.deleteById(id);
    }

    private void findByEmail(UserDTO userDTO){
        Optional<User> user =  userRepository.findByEmail(userDTO.getEmail());
        if(user.isPresent() && !user.get().getId().equals(userDTO.getId())){
            throw new DataIntegrationViolationException("Email já cadastrado");
        }
    }
}
