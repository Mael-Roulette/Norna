package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.space.CreateSpaceRequest;
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
            @RequestBody CreateSpaceRequest createSpaceRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spaceService.createSpace(authentication, createSpaceRequest));
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
}
