package br.com.anzolin.api.services;

import br.com.anzolin.api.domain.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User findById(Integer id);
}
