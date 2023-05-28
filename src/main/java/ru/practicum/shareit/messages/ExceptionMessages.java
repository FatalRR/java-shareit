package ru.practicum.shareit.messages;

public enum ExceptionMessages {
    ;
    public static final String INCORRECT_EMAIL = "Некорректный email адрес";
    public static final String EMPTY_NAME = "Имя не может быть пустым";
    public static final String EMPTY_DESCRIPTION = "Описание не может быть пустым";
    public static final String USER_NOT_FOUND = "Пользователя не существует";
    public static final String USER_EMAIL_EXIST = "Пользователь с таким email уже существует";
    public static final String ITEM_BE_AVAILABLE = "Вещь должна быть доступна";
    public static final String NOT_OWNER = "Вы не являетесь владельцем";
    public static final String NOT_ITEM = "Вещь с ID =%d не найдена";
    public static final String NOT_FUTURE_DATE = "Дата не может быть в будующем";
    public static final String NOT_EMPTY_DATE = "Дата не может быть пустой";
    public static final String NOT_FOUND_BOOKING = "Бронирование с ID =%d не найден";
    public static final String NOT_BOOKER = "Пользователь с ID =%d не является владельцом вещи";
    public static final String UNKNOWN_STATUS = "Unknown state: UNSUPPORTED_STATUS";
    public static final String NOT_EMPTY_TEXT = "Текст не может быть пустым";
    public static final String NOT_EMPTY_ID = "ID не может быть пустым";
    public static final String DATE_BOOKING = "Дата окончания бронирования должна быть после его начала";
    public static final String ALREADY_APPROVED = "Бронирование с ID =%d уже подтверждена";
    public static final String BOOKING_NOT_CONFIRMED = "Пользователь не брал в аренду вещь";
}