package br.com.anzolin.api.resources;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.domain.dto.UserDTO;
import br.com.anzolin.api.services.UserService;
import br.com.anzolin.api.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserResourceTest {

    public static final Integer ID = 1;
    public static final String NAME = "Marcelo";
    public static final String EMAIL = "marcelo@upf.br";
    public static final String PASSWORD = "123456";
    public static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";

    private User user = new User();
    private UserDTO userDTO = new UserDTO();

    @InjectMocks
    private UserResource userResource;

    @Mock
    private UserServiceImpl userService;

    @Mock //diz que não quero instancias reais
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        //moca o retorno do metodo findById
        Mockito.when(userService.findById(Mockito.anyInt())).thenReturn(user);
        Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userResource.findById(ID);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(ResponseEntity.class,response.getClass());
        Assertions.assertEquals(UserDTO.class,response.getBody().getClass());

        Assertions.assertEquals(ID,response.getBody().getId());
        Assertions.assertEquals(NAME,response.getBody().getName());
        Assertions.assertEquals(EMAIL,response.getBody().getEmail());
        Assertions.assertEquals(PASSWORD,response.getBody().getPassword());
    }

    @Test
    void whenFindAllThenReturnAListOfUserDTO() {
        //como se estive retornarndo uma lista de usuarios com um usuario
        Mockito.when(userService.findAll()).thenReturn(List.of(user));
        //converte para DTO
        Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);

        ResponseEntity<List<UserDTO>> response = userResource.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        //verifica se o objeto é um RESPONSEENTITYok
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(ResponseEntity.class,response.getClass());
        //verifica se o body retornou um ARRAYLIST
        assertEquals(ArrayList.class,response.getBody().getClass());

        //verfica se o objeto é um USERDTO
        assertEquals(UserDTO.class,response.getBody().get(0).getClass());

        assertEquals(ID,response.getBody().get(0).getId());
        assertEquals(NAME,response.getBody().get(0).getName());
        assertEquals(EMAIL,response.getBody().get(0).getEmail());
        assertEquals(PASSWORD,response.getBody().get(0).getPassword());
    }

    @Test
    void whenCreateThenReturnCreated() {
        Mockito.when(userService.create(Mockito.any())).thenReturn(user);
        ResponseEntity<UserDTO> response = userResource.create(userDTO);

        assertEquals(ResponseEntity.class,response.getClass());
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        Mockito.when(userService.update(userDTO)).thenReturn(user);
        Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userResource.update(ID,userDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(ResponseEntity.class,response.getClass());
        assertEquals(UserDTO.class,response.getBody().getClass());

        assertEquals(ID,response.getBody().getId());
        assertEquals(NAME,response.getBody().getName());
        assertEquals(EMAIL,response.getBody().getEmail());

    }

    @Test
    void whenDeleteThenReturnSuccess() {
        //ou seja não faça quando o service chamar o metodo delete, passandoqualquer valor inteiro como parametro
        Mockito.doNothing().when(userService).delete(Mockito.anyInt());

        ResponseEntity<UserDTO> response = userResource.delete(ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class,response.getClass());
        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyInt());
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    private void startUser(){
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID,NAME,EMAIL,PASSWORD);
    }
}