package com.example.demo.services;

import com.example.demo.filters.ProductFilter;
import com.example.demo.entities.User;
import com.example.demo.enums.SortingType;
import com.example.demo.products.Product;
import com.example.demo.products.ProductType;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

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
