package br.com.uanderson.service;

import br.com.uanderson.entities.User;
import br.com.uanderson.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getAllUsersPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User userDetails) {
        User user = getUserById(userDetails.getId());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setDni(userDetails.getDni());
        user.setActive(userDetails.getActive());
        user.setBirthDate(userDetails.getBirthDate());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(getUserById(id));
    }

    public User getUserByDni(String dni) {
        return userRepository.findUserByDni(dni);
    }

    public List<User> getUsersByBirthDateBetween(String startDate, String endDate) {
        return userRepository
                .findUsersByBirthDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }


}//class
