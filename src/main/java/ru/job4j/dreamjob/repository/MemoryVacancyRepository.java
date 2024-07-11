package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "интерн", LocalDateTime.now(), true));
        save(new Vacancy(0, "Junior Java Developer", "джун", LocalDateTime.now(), true));
        save(new Vacancy(0, "Junior+ Java Developer", "джун+", LocalDateTime.now(), true));
        save(new Vacancy(0, "Middle Java Developer", "миддл", LocalDateTime.now(), true));
        save(new Vacancy(0, "Middle+ Java Developer", "миддл+", LocalDateTime.now(), true));
        save(new Vacancy(0, "Senior Java Developer", "Cеньор", LocalDateTime.now(), true));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        vacancies.remove(id);
        return true;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> {
            return new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate(), vacancy.getVisible());
        }) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}