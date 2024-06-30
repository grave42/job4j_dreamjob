package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
    private final Map<Integer, Candidate> candidates = new HashMap<>();
    private int nextId = 1;

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Иванов Иваны Иваонвич", "интерн", LocalDateTime.now()));
        save(new Candidate(0, "Бобиков Боб Бобыч", "джун", LocalDateTime.now()));
        save(new Candidate(0, "Вахмурка Кржмелик", "джун+", LocalDateTime.now()));
        save(new Candidate(0, "Артур Пирожков", "миддл", LocalDateTime.now()));
        save(new Candidate(0, "Лютый Прогер", "миддл+", LocalDateTime.now()));
        save(new Candidate(0, "Марк Цукербер", "Cеньор", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
