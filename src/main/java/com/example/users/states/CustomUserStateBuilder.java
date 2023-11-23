package com.example.users.states;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Builder для свого власного стейту юзера (CustomUserState)
 */
public class CustomUserStateBuilder {
    /** Тека користувача, у якої до неї буде доступ*/
    private final String homeDir;
    /** Ім'я ролі, воно показуватиметься у консолі*/
    private final String name;
    /** Ім'я ролі, воно показуватиметься у консолі*/
    private boolean isEnabled = true;
    /**Чи адмін? (можливість заходити на повний сервер)*/
    private boolean isAdmin = false;
    /**Можливість створення/редагування файлів */
    private boolean canWrite = false;
    /** пропускна швидкість на вивантаження */
    int uploadSpeed = 0;
    /** пропускна швидкість на завантаження */
    int downloadSpeed = 0;

    public CustomUserStateBuilder(String homeDir, String name) {
        valid(homeDir, name);
        this.homeDir = homeDir;
        this.name = name;
    }

    /**
     * Встановити, чи включений аккаунт користувача (чи може зайти на сервер)
     @param isEnabled чи зможе користувач заходити на сервер.
     */
    public CustomUserStateBuilder setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    /**
     * Надати користувачу права адміна
     @param isAdmin чи адмін користувач.
     */
    public CustomUserStateBuilder setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    /**
     * Надати користувачу права створювати файли, їх редагувати
     @param canWrite чи може це користувач робити
     */
    public CustomUserStateBuilder setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }
    /**
     * Встановити швидкість вивантажування
     @param uploadSpeed швидкість вивантажування
     */
    public CustomUserStateBuilder setUploadSpeed(int uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
        return this;
    }
    /**
     * Встановити швидкість завантажування
     @param downloadSpeed швидкість завантажування
     */
    public CustomUserStateBuilder setDownloadSpeed(int downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
        return this;
    }
    /**
     * Отримати наш готовий стейт
     */
    public CustomUserState build() {
        return new CustomUserState(isEnabled,homeDir,isAdmin,uploadSpeed,downloadSpeed,canWrite,name);
    }

    /**
     * провалідувати вхідні дані перед тим, як створити об'єкт
     * @throws NullPointerException аргумент null
     * @throws IllegalArgumentException аргумент не відповідає критеріям
     */
    private void valid(String homeDir, String name){
        if (name == null){
            throw new NullPointerException("name is null");
        }
        if (name.trim().isEmpty()){
            throw new IllegalArgumentException("name is empty");
        }
        if (homeDir == null){
            throw new NullPointerException("homeDir is null");
        }
        Path path = Paths.get(homeDir);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Такого шляху не існує");
        }
    }
}

