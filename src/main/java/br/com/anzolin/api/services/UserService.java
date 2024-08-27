package br.com.anzolin.api.services;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.domain.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User findById(Integer id);
    List<User> findAll();
    User create(UserDTO userDto);
    User update(UserDTO userDto);
    void delete(Integer id);
}
