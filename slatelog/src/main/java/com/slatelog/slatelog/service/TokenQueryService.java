package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.security.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.mongodb.internal.HexUtils.toHex;

@Service
@RequiredArgsConstructor
public class TokenQueryService {

    private final EventRepository eventRepository;

    public boolean checkForValidToken(String eventId, String token){
        System.out.println("we are in the tokenQuery");
        for (Invitation invitation : Objects.requireNonNull(eventRepository.getEventById(eventId).getInvitations())) {
            assert invitation.getInvitationToken() != null;
            String invitationToken = invitation.getInvitationToken().getEncodedValue().toString().trim();
            String inputToken = token.trim().replace(" ", "+");
            if (invitationToken.equals(inputToken))
                return true;
        }
        return false;
    }

    public String checkForValidVoterEmail(String eventId, String token){
        for (Invitation invitation : Objects.requireNonNull(eventRepository.getEventById(eventId).getInvitations())) {
            assert invitation.getInvitationToken() != null;
            String invitationToken = invitation.getInvitationToken().getEncodedValue();
            String inputToken = token.replace(" ", "+");
            if (invitationToken.equals(inputToken)) {
                return invitation.getEmail();
            }
        }
        return "false";
    }

    public Invitation checkForInvitation(String eventId, String token){
        for (Invitation invitation : Objects.requireNonNull(eventRepository.getEventById(eventId).getInvitations())) {
            assert invitation.getInvitationToken() != null;
            String invitationToken = invitation.getInvitationToken().getEncodedValue();
            String inputToken = token.replace(" ", "+");
            if (invitationToken.equals(inputToken)) {
                return invitation;
            }
        }
        return null;
    }

}
