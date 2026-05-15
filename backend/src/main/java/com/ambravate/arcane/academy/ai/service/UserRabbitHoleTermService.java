package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.RabbitHoleTermDto;
import com.ambravate.polymath.academy.model.UserRabbitHoleTerm;
import com.ambravate.polymath.academy.repository.UserRabbitHoleTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRabbitHoleTermService {

    private final UserRabbitHoleTermRepository repository;

    public List<RabbitHoleTermDto> getAll(String userId) {
        return repository.findByUserIdOrderBySavedAtDesc(userId)
                .stream().map(RabbitHoleTermDto::from).toList();
    }

    @Transactional
    public RabbitHoleTermDto save(String userId, String term, String description,
                                   String subChunkId, String topicId) {
        return repository.findByUserIdAndTerm(userId, term)
                .map(existing -> {
                    existing.setDescription(description);
                    existing.setSubChunkId(subChunkId);
                    existing.setTopicId(topicId);
                    return RabbitHoleTermDto.from(repository.save(existing));
                })
                .orElseGet(() -> {
                    UserRabbitHoleTerm saved = repository.save(
                            UserRabbitHoleTerm.builder()
                                    .userId(userId)
                                    .term(term)
                                    .description(description)
                                    .subChunkId(subChunkId)
                                    .topicId(topicId)
                                    .build());
                    return RabbitHoleTermDto.from(saved);
                });
    }

    @Transactional
    public void remove(String userId, String term) {
        repository.deleteByUserIdAndTerm(userId, term);
    }
}
