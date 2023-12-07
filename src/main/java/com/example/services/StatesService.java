package com.example.services;

import com.example.entity.CustomStateEntity;
import com.example.repositories.CustomStateRepository;
import com.example.users.states.AbstractUserState;
import com.example.users.states.CustomUserState;
import com.example.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class StatesService {
    private ApplicationContext applicationContext;
    private CustomStateRepository customStateRepository;

    public List<AbstractUserState> getAllDefaultStates() {
        Map<String, AbstractUserState> beans = applicationContext.getBeansOfType(AbstractUserState.class);
        return new ArrayList<>(beans.values());
    }

    /**
     * Віддає стан користувача, як дефолтний, так і кастомний
     * @param name
     * @return
     */
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

    public boolean doesStateExist(String name){
        return customStateRepository.getByName(name).isPresent();
    }

    public Optional<CustomUserState> getCustomStateByName(String name) {
        return customStateRepository.getByName(name).map(CustomUserState::new);
    }

    /**
     * Створює новий запис
     * Якщо ім'я співпаде з дефолтним іменем, кине помилку
     * @param customUserState кастомний стейт
     */
    public void createState(CustomUserState customUserState){
        if (getAllDefaultStates().stream().map(AbstractUserState::getName).noneMatch(o -> o.equals(customUserState.getName()))){
            throw new StateCreationException("Custom state has same name, as default: "+ customUserState.getName());
        }
        if (customStateRepository.getByName(customUserState.getName()).isPresent()){
            throw new StateCreationException("There is already state with this name: "+ customUserState.getName());
        }
       customStateRepository.save(new CustomStateEntity(customUserState));
    }


    public CustomStateEntity convertCustomUserState(CustomUserState customUserState){
        return new CustomStateEntity(customUserState);
    }


}
