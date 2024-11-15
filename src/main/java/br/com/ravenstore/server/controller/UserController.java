package br.com.ravenstore.server.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ravenstore.server.dto.UserDTO;
import br.com.ravenstore.server.dto.UserResponseDTO;
import br.com.ravenstore.server.model.User;
import br.com.ravenstore.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/users")
public class UserController {
  private final UserService userService;
  private final ModelMapper modelMapper;

  private UserResponseDTO convertToResponseDto(User user) {
    return modelMapper.map(user, UserResponseDTO.class);
  }

  private User convertToEntity(UserDTO userDTO) {
    return modelMapper.map(userDTO, User.class);
  }

  public UserController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDTO> findOne(@PathVariable Long id) {
    User user = userService.findOne(id);
    if (user != null) {
      return ResponseEntity.ok(convertToResponseDto(user));
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserDTO userDTO) {
    User savedUser = userService.save(convertToEntity(userDTO));
    return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDto(savedUser));
  }

  @PutMapping("{id}")
  public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
    User user = userService.findOne(id);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    userDTO.setId(id);
    User updatedUser = userService.save(convertToEntity(userDTO));
    return ResponseEntity.ok(convertToResponseDto(updatedUser));
  }

}
