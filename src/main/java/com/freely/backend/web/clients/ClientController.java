package com.freely.backend.web.clients;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freely.backend.client.ClientService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.ClientDTO;
import com.freely.backend.web.clients.dto.ClientForm;

@RestController
@RequestMapping("/clients")
public class ClientController {
  @Autowired
  private ClientService clientService;

  @GetMapping
  public ResponseEntity<Page<ClientDTO>> index(
      @PageableDefault(page = 0, size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam String query,
      @AuthenticationPrincipal UserAccount user) {

    Page<ClientDTO> clients = clientService.findAll(user, query, pageable);

    return ResponseEntity.ok(clients);
  }

  @PostMapping
  public ResponseEntity<ClientDTO> store(@AuthenticationPrincipal UserAccount user,
      @Valid @RequestBody ClientForm form) {

    ClientDTO createClient = clientService.create(form, user);

    return ResponseEntity.status(HttpStatus.CREATED).body(createClient);
  }

  @PutMapping("/{clientId}")
  public ResponseEntity<ClientDTO> update(@AuthenticationPrincipal UserAccount user,
      @Valid @RequestBody ClientForm form, @PathVariable UUID clientId) {

    ClientDTO updatedClient = clientService.update(form, clientId, user);

    return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
  }

  @DeleteMapping("/{clientId}")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user, @PathVariable UUID clientId) {
    clientService.delete(clientId, user);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
