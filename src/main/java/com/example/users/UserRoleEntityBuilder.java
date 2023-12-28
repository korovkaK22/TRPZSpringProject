package com.example.users;

import com.example.entity.RoleEntity;
import com.example.users.UserRole;

/**
 * Builder для свого власного стейту юзера (CustomUserState)
 */
public class UserRoleEntityBuilder {
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
    int uploadSpeed = 100;
    /** пропускна швидкість на завантаження */
    int downloadSpeed = 100;

    public UserRoleEntityBuilder(String name, String homeDir) {
        valid(homeDir, name);
        this.homeDir = homeDir;
        this.name = name;
    }

    /**
     * Встановити, чи включений аккаунт користувача (чи може зайти на сервер)
     @param isEnabled чи зможе користувач заходити на сервер.
     */
    public UserRoleEntityBuilder setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    /**
     * Надати користувачу права адміна
     @param isAdmin чи адмін користувач.
     */
    public UserRoleEntityBuilder setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    /**
     * Надати користувачу права створювати файли, їх редагувати
     @param canWrite чи може це користувач робити
     */
    public UserRoleEntityBuilder setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }
    /**
     * Встановити швидкість вивантажування
     @param uploadSpeed швидкість вивантажування >=0
     */
    public UserRoleEntityBuilder setUploadSpeed(int uploadSpeed) {
        if (uploadSpeed < 0) {
            throw new IllegalArgumentException("speed cannot be minus");
        }
        this.uploadSpeed = uploadSpeed;
        return this;
    }
    /**
     * Встановити швидкість завантажування
     @param downloadSpeed швидкість завантажування >=0
     */
    public UserRoleEntityBuilder setDownloadSpeed(int downloadSpeed) {
        if (downloadSpeed < 0) {
            throw new IllegalArgumentException("speed cannot be minus");
        }
        this.downloadSpeed = downloadSpeed;
        return this;
    }
    /**
     * Отримати наш готовий стейт
     */
    public RoleEntity build() {
        RoleEntity result = new RoleEntity();
        result.setIsAdmin(isAdmin);
        result.setCanWrite(canWrite);
        result.setDownloadSpeed(downloadSpeed);
        result.setUploadSpeed(uploadSpeed);
        result.setPath(homeDir);
        result.setIsEnabled(isEnabled);
        result.setName(name);
        return  result;
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
    }
}