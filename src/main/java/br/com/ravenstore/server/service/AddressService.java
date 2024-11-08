package br.com.ravenstore.server.service;

import org.springframework.stereotype.Service;

import br.com.ravenstore.server.model.Address;
import br.com.ravenstore.server.repository.AddressRepository;

@Service
public class AddressService {
  private final AddressRepository addressRepository;

  public AddressService(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  public Address save(Address address) {
    return addressRepository.save(address);
  }
}
