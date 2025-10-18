package com.example.funding.validator;

import com.example.funding.exception.forbidden.AccessDeniedException;
import org.springframework.stereotype.Component;

import static com.example.funding.validator.Preconditions.require;

@Component
public class PermissionChecker {
    /**
     * 현재 액터가 리소스의 소유자인지 검증.
     *
     * @param actorId 현재 액터의 ID
     * @param ownerId 리소스 소유자의 ID
     * @throws AccessDeniedException 액터가 소유자가 아닌 경우 발생
     */
    public void mustBeOwner(Long actorId, Long ownerId) {
        require(actorId != null && actorId.equals(ownerId), AccessDeniedException::new);
    }

    public void mustBeAdmin(boolean isAdmin) {
        require(isAdmin, AccessDeniedException::new);
    }
}
