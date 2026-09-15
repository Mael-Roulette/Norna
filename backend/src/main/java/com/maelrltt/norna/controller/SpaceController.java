package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.space.SpaceRequest;
import com.maelrltt.norna.dto.space.SpaceResponse;
import com.maelrltt.norna.dto.space.SpaceResponseWithDetails;
import com.maelrltt.norna.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;

    @PostMapping()
    public ResponseEntity<SpaceResponseWithDetails> createSpace(
            Authentication authentication,
            @RequestBody SpaceRequest spaceRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spaceService.createSpace(authentication, spaceRequest));
    }

    @GetMapping()
    public ResponseEntity<List<SpaceResponse>> getSpaces (
            Authentication authentication
    ) {
        return ResponseEntity.ok(spaceService.getSpaces(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponseWithDetails> getSpaceById(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(spaceService.getSpaceById(authentication, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SpaceResponseWithDetails> updateSpace(
            @PathVariable UUID id,
            @RequestBody SpaceRequest spaceRequest,
            Authentication authentication
    ) {
        return ResponseEntity.ok(spaceService.updateSpace(authentication, id, spaceRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpace(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        spaceService.deleteSpace(authentication, id);

        return ResponseEntity.noContent().build();
    }
}
