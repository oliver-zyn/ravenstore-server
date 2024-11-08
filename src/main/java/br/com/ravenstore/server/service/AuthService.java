package br.com.ravenstore.server.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ravenstore.server.model.User;
import br.com.ravenstore.server.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {
  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(email);
    
    if (user == null) {
      throw new UsernameNotFoundException("Usuário não encontrado!");
    }

    return user;
  }
  
}