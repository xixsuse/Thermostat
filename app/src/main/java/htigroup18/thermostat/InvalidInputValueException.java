/**
 * @author HTI students, Spring 2013
 *
 */
package htigroup18.thermostat;

public class InvalidInputValueException extends Exception {

    public InvalidInputValueException() {
        super();
    }

    public InvalidInputValueException(String msg) {
        super(msg);
    }

    public InvalidInputValueException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputValueException(Throwable cause) {
        super(cause);
    }
}