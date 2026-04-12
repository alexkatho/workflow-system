package com.portfolio.workflow.request.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.workflow.request.application.dto.ApproveRequestDto;
import com.portfolio.workflow.request.application.dto.CreateRequestRequestDto;
import com.portfolio.workflow.request.application.dto.RejectRequestDto;
import com.portfolio.workflow.request.application.dto.RequestResponseDto;
import com.portfolio.workflow.request.application.mapper.RequestApplicationMapper;
import com.portfolio.workflow.request.application.service.RequestService;
import com.portfolio.workflow.request.domain.model.Request;
import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.user.application.exception.CurrentUserNotFoundException;
import com.portfolio.workflow.user.application.service.UserService;
import com.portfolio.workflow.user.domain.model.User;

import jakarta.validation.Valid;

/**
 * REST Controller für fachliche Requests.
 */
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    private final UserService userService;

    public RequestController(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<RequestResponseDto> createRequest(@Valid @RequestBody CreateRequestRequestDto dto,
                                                            Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        Request createdRequest = requestService.createRequest(
                dto.title(),
                dto.description(),
                dto.type(),
                currentUser.getId()
        );

        return ResponseEntity.ok(RequestApplicationMapper.toResponseDto(createdRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<RequestResponseDto> getRequestById(@PathVariable UUID id,
                                                             Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        return requestService.findVisibleById(id, currentUser)
                .map(RequestApplicationMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * USER sieht nur eigene Requests.
     * MANAGER und ADMIN sehen alle Requests.
     * Optional kann nach Status gefiltert werden.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<RequestResponseDto> getRequests(@RequestParam(required = false) RequestStatus status,
                                                Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        List<Request> requests = requestService.findVisibleRequests(currentUser, status);

        return requests.stream()
                .map(RequestApplicationMapper::toResponseDto)
                .toList();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<RequestResponseDto> approveRequest(@PathVariable UUID id,
                                                             @Valid @RequestBody ApproveRequestDto dto,
                                                             Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        Request updatedRequest = requestService.approveRequest(
                id,
                currentUser.getId(),
                dto.decisionComment()
        );

        return ResponseEntity.ok(RequestApplicationMapper.toResponseDto(updatedRequest));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<RequestResponseDto> rejectRequest(@PathVariable UUID id,
                                                            @Valid @RequestBody RejectRequestDto dto,
                                                            Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        Request updatedRequest = requestService.rejectRequest(
                id,
                currentUser.getId(),
                dto.decisionComment()
        );

        return ResponseEntity.ok(RequestApplicationMapper.toResponseDto(updatedRequest));
    }
    
    /**
     * Storniert einen offenen Request.
     *
     * <p>
     * Nur der Ersteller darf seinen eigenen Request stornieren.
     * </p>
     */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<RequestResponseDto> cancelRequest(@PathVariable UUID id,
                                                            Authentication authentication) {

        var currentUser = getCurrentUser(authentication);

        Request updatedRequest = requestService.cancelRequest(id, currentUser);

        return ResponseEntity.ok(RequestApplicationMapper.toResponseDto(updatedRequest));
    }

    private User getCurrentUser(Authentication authentication) {
        String currentUserEmail = authentication.getName();

        return userService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new CurrentUserNotFoundException(currentUserEmail));
    }
}