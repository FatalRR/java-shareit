package ru.practicum.gateway.messages;

public enum ExceptionMessages {
    ;
    public static final String INCORRECT_EMAIL = "Некорректный email адрес";
    public static final String EMPTY_NAME = "Имя не может быть пустым";
    public static final String EMPTY_DESCRIPTION = "Описание не может быть пустым";
    public static final String USER_EMAIL_EXIST = "Пользователь с таким email уже существует";
    public static final String NOT_FUTURE_DATE = "Дата не может быть в будующем";
    public static final String NOT_EMPTY_DATE = "Дата не может быть пустой";
    public static final String NOT_EMPTY_TEXT = "Текст не может быть пустым";
    public static final String NOT_EMPTY_ID = "ID не может быть пустым";
}