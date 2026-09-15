package com.maelrltt.norna.controller;

import com.maelrltt.norna.dto.spaceMember.RemoveSpaceMemberRequest;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberRequest;
import com.maelrltt.norna.dto.spaceMember.SpaceMemberDetailsResponse;
import com.maelrltt.norna.entity.SpaceMember;
import com.maelrltt.norna.service.SpaceMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/space-member")
@RequiredArgsConstructor
public class SpaceMemberController {
    private final SpaceMemberService spaceMemberService;

    @PostMapping("/{spaceId}/members")
    public ResponseEntity<SpaceMemberDetailsResponse> addSpaceMember(
            @PathVariable UUID spaceId,
            @RequestBody SpaceMemberRequest request,
            Authentication authentication
    ) {
        SpaceMember spaceMember =
                spaceMemberService.addMember(
                        spaceId,
                        request.userId(),
                        request.role(),
                        authentication
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SpaceMemberDetailsResponse(
                        spaceMember.getId(),
                        spaceMember.getUser().getUsername(),
                        spaceMember.getUser().getEmail(),
                        spaceMember.getRole()
                )
        );
    }

    @PatchMapping("/{spaceId}/members")
    public ResponseEntity<SpaceMemberDetailsResponse> updateSpaceMember(
            @PathVariable UUID spaceId,
            @RequestBody SpaceMemberRequest request,
            Authentication authentication
    ) {
        SpaceMember spaceMember = this.spaceMemberService.updateMember(
                spaceId,
                request.userId(),
                request.role(),
                authentication
        );

        return ResponseEntity.ok(
                new SpaceMemberDetailsResponse(
                        spaceMember.getId(),
                        spaceMember.getUser().getUsername(),
                        spaceMember.getUser().getEmail(),
                        spaceMember.getRole()
                )
        );
    }

    @DeleteMapping("/{spaceId}/members")
    public void removeMember(
            @PathVariable UUID spaceId,
            @RequestBody RemoveSpaceMemberRequest request,
            Authentication authentication
    ) {
        this.spaceMemberService.removeMember(
                spaceId,
                request.userId(),
                authentication
        );
    }
}
