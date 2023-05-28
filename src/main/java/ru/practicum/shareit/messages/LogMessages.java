package ru.practicum.shareit.messages;

public enum LogMessages {
    COUNT("Количество объектов: {}"),
    TRY_GET_OBJECT("Попытка получить объект: {}"),
    TRY_ADD("Попытка добавить: {}"),
    TRY_UPDATE("Попытка обновить: {}"),
    TRY_REMOVE("Попытка удалить: {}"),
    ERROR_404("Ошибка 404: {}"),
    ERROR_400("Ошибка 400: {}"),
    ERROR_409("Ошибка 409: {}"),
    ERROR_500("Ошибка 500: {}"),
    TRY_GET_BOOKING("Потытка получить бронирование с ID: {}"),
    TRY_GET_BOOKING_BY_USER_ID("Потытка получить бронирование по ID пользователя: {}"),
    TRY_GET_BOOKING_BY_OWNER_ID("Потытка получить бронирование по ID владельца: {}"),
    TRY_APPROVE("Попытка подтвердить бронирование с ID {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}