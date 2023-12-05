package com.example.services;

import com.example.entity.CustomStateEntity;
import com.example.repositories.CustomStateRepository;
import com.example.repositories.UserRepository;
import com.example.users.states.AbstractUserState;
import com.example.users.states.CustomUserState;
import com.example.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatesService {
    private ApplicationContext applicationContext;
    private CustomStateRepository customStateRepository;

    public List<AbstractUserState> getAllDefaultStates() {
        Map<String, AbstractUserState> beans = applicationContext.getBeansOfType(AbstractUserState.class);
        return new ArrayList<>(beans.values());
    }

    public AbstractUserState getStateByName(String name) throws StateNotFoundException{
        //Шукаємо по дефолтам
        Optional<? extends AbstractUserState> defaultState = getAllDefaultStates()
                .stream()
                .filter(state -> name.equals(state.getName()))
                .findAny();
        if (defaultState.isPresent()) {
            return defaultState.get();
        }
        //Шукаємо по кастомним
        Optional<CustomUserState> customState = getCustomStateByName(name);
        if (customState.isPresent()) {
            return customState.get();
        }
        //Не знайшли, кидаємо помилку
        throw new StateNotFoundException("State not found with name: " + name);
    }

    public Optional<CustomUserState> getCustomStateByName(String name) {
        return customStateRepository.getByName(name).map(CustomUserState::new);
    }

    /**
     * Створює новий запис, або обновлює попередні дані
     * @param customUserState
     */
    public void save(CustomUserState customUserState){
       customStateRepository.save(new CustomStateEntity(customUserState));
    }

}
