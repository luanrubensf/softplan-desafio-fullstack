package com.luanrubensf.challenge.api;


import com.luanrubensf.challenge.model.User;
import com.luanrubensf.challenge.repository.UserRepository;
import com.luanrubensf.challenge.representation.UserDto;
import com.luanrubensf.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDto.Representation representation;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAllBy();
        return representation.toRepresentation(users);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getById(@PathVariable("id") Long id) {
        User user = userRepository.findUserById(id);
        return representation.toRepresentation(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto insert(@RequestBody UserDto dto) {
        User user = representation.fromRepresentation(dto);
        User saved = userService.save(user, dto.getRole());
        return representation.toRepresentation(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto dto) {
        User userById = userRepository.findUserById(id);
        User user = representation.fromRepresentation(dto, User.Builder.from(userById));
        User saved = userService.save(user, dto.getRole());
        return representation.toRepresentation(saved);
    }
}
