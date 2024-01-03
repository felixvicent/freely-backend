package com.freely.backend.web.clients;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.freely.backend.suggestion.dto.SuggestionDTO;
import com.freely.backend.web.clients.dto.ClientPageDTO;
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
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.clients.dto.ClientForm;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientListDTO>> index(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @AuthenticationPrincipal UserAccount user) {

        Page<ClientListDTO> clients = clientService.findAll(user, query, pageable);

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientPageDTO> show(@PathVariable UUID clientId, @AuthenticationPrincipal UserAccount user) {
        ClientPageDTO client = clientService.findById(clientId, user);

        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PostMapping
    public ResponseEntity<ClientListDTO> store(@AuthenticationPrincipal UserAccount user,
                                               @Valid @RequestBody ClientForm form) {

        ClientListDTO createClient = clientService.create(form, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientListDTO> update(@AuthenticationPrincipal UserAccount user,
                                                @Valid @RequestBody ClientForm form, @PathVariable UUID clientId) {

        ClientListDTO updatedClient = clientService.update(form, clientId, user);

        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user, @PathVariable UUID clientId) {
        clientService.delete(clientId, user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/suggestion")
    public ResponseEntity<List<SuggestionDTO>> getSuggestion(
            @AuthenticationPrincipal UserAccount user,
            @RequestParam String query,
            @RequestParam(required = false) UUID selectedClientId) {

        if (selectedClientId != null) {
            ClientPageDTO client = clientService.findById(selectedClientId, user);

            List<SuggestionDTO> clients = clientService.getSuggestion(client.getName(), user);

            return ResponseEntity.status(HttpStatus.OK).body(clients);
        }
        List<SuggestionDTO> clients = clientService.getSuggestion(query, user);

        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

}
