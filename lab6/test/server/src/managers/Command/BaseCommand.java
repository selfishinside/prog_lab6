package managers.Command;

import system.Request;

/**
 * Интерфейс для реализации команд.
 *
 * @author wrakelft
 * @since 1.0
 */
public interface BaseCommand {

    /**
     * Базовый метод для вывода исполнения команды
     * Выбрасывает ошибки при реализации
     *
     * @author wrakelft
     * @since 1.0
     */
    public String execute(Request request) throws Exception;

    /**
     * Базовый метод для вывода названия команды
     *
     * @author wrakelft
     * @since 1.0
     */
    public String getName();

    /**
     * Базовый метод для вывода описания команды
     *
     * @author wrakelft
     * @since 1.0
     */
    public String getDescription();


}
