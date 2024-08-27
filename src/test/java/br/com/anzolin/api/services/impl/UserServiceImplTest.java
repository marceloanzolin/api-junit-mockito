package br.com.anzolin.api.services.impl;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.domain.dto.UserDTO;
import br.com.anzolin.api.repositories.UserRepository;
import br.com.anzolin.api.services.exceptions.DataIntegrationViolationException;
import br.com.anzolin.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    public static final Integer ID = 1;
    public static final String NAME = "Marcelo";
    public static final String EMAIL = "marcelo@upf.br";
    public static final String PASSWORD = "123456";
    public static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock //estou mocando no caso não preciso acessar os dados reais da base de dados
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach //antes de tudo
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test // teste quando fizer a busca por id me retorne a busca por um usuário
    void whenFindByIdThenReturnAnUserInstance() {
        //quando o metodo for chamado
        //Mockito.anyInt() qualquer numero inteiro me retorne um optional de um usuário
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(optionalUser);

        //chamei o metodo
        User response = userServiceImpl.findById(ID);

        assertNotNull(response);//assegure para mim que o response não será nullo
        //assegure para mim que ambos são iguais (1º e segundo argumento)
        //verificandos e vai retornar um objeto que a classe seja do tipo User
        assertEquals(User.class,response.getClass());

        //assegure que o ID da resposta seja o mesmo ID que eu passei por parametro
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());

    }

    @Test //retorne a excessãod e um objeto não encontrado
    void whenFindByIdThenReturnAnObjetcNotFoundException() {
        //mocar a resposta
        //quando chamar o metodo findByID estoura uma excessão do tipo ObjectNotFoundException
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenThrow(new ObjectNotFoundException(USUARIO_NAO_ENCONTRADO));

        try {
            userServiceImpl.findById(ID);
        } catch (Exception ex) {
            //assegure para mim que a excessão que foi estourada é do tipo ObjectNotFoundException
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            //assegure para mim que as mensagens são iguais
            assertEquals(USUARIO_NAO_ENCONTRADO, ex.getMessage());

        }
    }

    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        //mocar a resposta
        //quando chamar o metodo findAll me retorne uma lista de usuários
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> response = userServiceImpl.findAll();
        //verifcar que a ressta não pe nulla
        assertNotNull(response);
        //assegurar que o tamnho da lista seja 1
        assertEquals(1,response.size());
        //assegurar que o primeiro elemento da lista seja do tipo User
        assertEquals(User.class,response.get(0).getClass());

        assertEquals(ID,response.get(0).getId());
        assertEquals(NAME,response.get(0).getName());
        assertEquals(EMAIL,response.get(0).getEmail());
        assertEquals(PASSWORD,response.get(0).getPassword());
    }

    @Test
    void whenCreateThenReturnSucess() {
        //moca a resposta do save
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        User response = userServiceImpl.create(userDTO);
        //assegurar que a resosta nao nulla
        assertNotNull(response);
        assertEquals(User.class,response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(PASSWORD,response.getPassword());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2); //isso pq quando eu salvo ele verifica se o id é o mesmo passado por parametro
            userServiceImpl.create(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegrationViolationException.class, ex.getClass());
            assertEquals("Email já cadastrado", ex.getMessage());
        }
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        //moca a resposta do save
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        User response = userServiceImpl.update(userDTO);

        //assegurar que a resosta nao nulla
        assertNotNull(response);
        assertEquals(User.class,response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(PASSWORD,response.getPassword());
    }

    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try {
            //da para colocar retornar do findbyid
            optionalUser.get().setId(2); //isso pq quando eu atualizo ele verifica se o id é o mesmo passado por parametro
            userServiceImpl.update(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegrationViolationException.class, ex.getClass());
            assertEquals(EMAIL_JA_CADASTRADO, ex.getMessage());
        }
    }

    @Test
    void deleteWithSuccess() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(optionalUser);
        //o findbyid do delete não retorna nada
        //não faça nada quando o repository for chamado o metodo deleteById,
        //fazendo isso para nao dar erro de registro não entontrado
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyInt());
        userServiceImpl.delete(ID);//como ele não te retorno eu preciso verifcar de outra forma
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(ID);//verififca quantas vezes o metodo foi chamado
    }

    @Test
    void deleteWithObjectNotFoundException() {
        //mocar a resposta
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenThrow(new ObjectNotFoundException(USUARIO_NAO_ENCONTRADO));

        try{
            userServiceImpl.delete(ID);
        }catch (Exception ex){
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(USUARIO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    private void startUser(){
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID,NAME,EMAIL,PASSWORD);
        optionalUser = Optional.of(new User(ID,NAME,EMAIL,PASSWORD));
    }
}