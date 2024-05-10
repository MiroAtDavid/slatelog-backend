package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.security.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenQueryService {

    private final EventRepository eventRepository;

    public boolean CheckForValidToken(String eventId, String token){
        for (Invitation invitation : Objects.requireNonNull(eventRepository.getEventById(eventId).getInvitations())) {
            assert invitation.getInvitationToken() != null;
            String invitationToken = invitation.getInvitationToken().getEncodedValue().toString().trim();
            String inputToken = token.trim();
            if (invitationToken.equals(inputToken))
                return true;
        }
        return false;
    }
}
