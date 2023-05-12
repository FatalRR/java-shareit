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
    ERROR_500("Ошибка 500: {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}