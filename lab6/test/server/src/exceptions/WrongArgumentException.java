package exceptions;

/**
 * Обеспечивает исключение, если возникла ошибка в аргументе
 *
 * @since 1.0
 */

public class WrongArgumentException extends Exception{

    /**
     * @param argument название аргумента, который был введен с ошибкой
     *
     * @since 1.0
     */
    public WrongArgumentException(String argument) {
        super("Wrong argument in command: " + argument);
    }
}
