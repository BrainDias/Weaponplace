package org.weaponplace.services.implementations;

import org.weaponplace.entities.User;
import org.weaponplace.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;


    public Page<User> pageUsers(Pageable pageRequest){
        return userRepository.findAll(pageRequest);
    }

    public void banUser(Long id) {
        userRepository.findById(id).ifPresent(User -> {
            User.setActive(false);
            userRepository.save(User);
        });
    }


    @Cacheable("users")
    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("User doesn't exist");
        User selectedUser = optionalUser.get();
        return selectedUser;
    }

    public void updateRating(Long userId, int newRating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Текущий суммарный рейтинг до добавления новой оценки
        float currentTotalRating = user.getRating() * user.getRatingsNumber();

        // Обновляем количество оценок
        int updatedRatingsNumber = user.getRatingsNumber() + 1;

        // Пересчитываем новый средний рейтинг
        float updatedRating = (currentTotalRating + newRating) / updatedRatingsNumber;

        // Обновляем значения у пользователя
        user.setRating(updatedRating);
        user.setRatingsNumber(updatedRatingsNumber);

        // Сохраняем изменения в базе данных
        userRepository.save(user);
    }
}
