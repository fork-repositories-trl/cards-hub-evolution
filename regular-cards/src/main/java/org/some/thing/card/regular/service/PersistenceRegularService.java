package org.some.thing.card.regular.service;

import lombok.RequiredArgsConstructor;
import org.some.thing.card.entity.CardType;
import org.some.thing.card.entity.UserData;
import org.some.thing.card.regular.configuration.RegularProperties;
import org.some.thing.card.regular.database.RegularRepository;
import org.some.thing.card.regular.entity.RegularCard;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersistenceRegularService implements RegularService {
    private final RegularProperties properties;
    private final RegularRepository regularRepository;
    private static final long DAY = 86_400_000;

    @Override
    public List<RegularCard> loadRegular(UserData userData) {
        Date from = new Date(userData.getCurrentDate() - DAY);
        Date to = new Date(userData.getCurrentDate() + DAY);
        return regularRepository.findByUserIdAndDueDateBetween(userData.getUserId(), from, to).stream()
                .map(doc ->
                        RegularCard.builder()
                                .userId(userData.getUserId())
                                .dueDate(doc.getDueDate().getTime())
                                .amount(doc.getAmount())
                                .targetAccount(doc.getTargetAccount())
                                .id(doc.getId())
                                .executionUrl(properties.getExecuteUrl())
                                .type(CardType.REGULAR)
                                .build()
                ).collect(Collectors.toList());
    }
}
