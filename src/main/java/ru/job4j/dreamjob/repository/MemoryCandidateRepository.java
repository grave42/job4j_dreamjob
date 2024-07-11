package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Иванов Иваны Иваонвич", "интерн", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Бобиков Боб Бобыч", "джун", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Вахмурка Кржмелик", "джун+", LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Артур Пирожков", "миддл", LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Лютый Прогер", "миддл+", LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Марк Цукербер", "Cеньор", LocalDateTime.now(), 3, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        candidates.remove(id);
        return true;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate(), candidate.getCityId(), candidate.getFileId())) != null;
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
