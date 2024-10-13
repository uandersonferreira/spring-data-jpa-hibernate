package br.com.uanderson.controller;

import br.com.uanderson.dao.UserDAO;
import br.com.uanderson.entities.User;
import br.com.uanderson.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final UserDAO userDAO;

    public UserController(UserService userService, UserDAO userDAO) {
        this.userService = userService;
        this.userDAO = userDAO;
    }

    // Recupera todos os usuários sem paginação
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Status 200 OK
    }

    // Recupera todos os usuários com paginação
    @GetMapping("/page")
    public ResponseEntity<Page<User>> getAllUsersPageable(Pageable pageable) {
        Page<User> usersPageable = userService.getAllUsersPageable(pageable);
        return new ResponseEntity<>(usersPageable, HttpStatus.OK);
    }

    // Recupera um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user); // Status 200 OK
    }

    // Cria um novo usuário
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // Status 201 Created
    }

    // Atualiza um usuário existente
    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser); // Status 200 OK
    }

    // Deleta um usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Status 204 No Content
    }

    @GetMapping("/active")
    private ResponseEntity<List<User>> findAllActive() {
        return new ResponseEntity<>(userDAO.findAllActive(), HttpStatus.OK);
    }

    @GetMapping("/dni/{dni}")
    private ResponseEntity<User> getUserByDni(@PathVariable String dni) {
        return new ResponseEntity<>(userService.getUserByDni(dni), HttpStatus.OK);
    }

    @GetMapping("/birthdate")
    private ResponseEntity<List<User>> getUsersByBirthDateBetween(@RequestParam String startDate,
                                                                  @RequestParam String endDate) {
        List<User> users = userService.getUsersByBirthDateBetween(startDate, endDate);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }



}//class
