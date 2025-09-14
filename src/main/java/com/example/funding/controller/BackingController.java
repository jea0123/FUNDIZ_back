package com.example.funding.controller;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import com.example.funding.service.BackingService;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/Backing")
@RequiredArgsConstructor
public class BackingController {
    private final BackingService backingService;
    //private final ProjectService projectService;
    //private final UserService userService;

    @GetMapping("/CreateBacking/{userId}/{projectId}")
    public ResponseEntity<ResponseDto<BackingResponseDto>>prepareBacking(@PathVariable Long projectId,
                                                                         @PathVariable Long userId) {
        return backingService.prepareBacking(userId, projectId);
    }

    @PostMapping("/CreateBacking")
    public ResponseEntity<ResponseDto<String>>CreateBacking(@RequestBody BackingRequestDto requestDto,
                                                            @PathVariable Long userId) {
        return backingService.createBacking(userId, requestDto);
    }
}
