package javax0.geci.jamal.reflection;

import javax0.jamal.api.BadSyntax;
import javax0.jamal.api.Input;
import javax0.jamal.api.Macro;
import javax0.jamal.api.Processor;

import static javax0.jamal.tools.InputHandler.skipWhiteSpaces;

/**
 * Upper case the fist character of the input.
 */
public class Cap implements Macro {
    @Override
    public String evaluate(Input in, Processor processor) throws BadSyntax {
        skipWhiteSpaces(in);
        final var sb = in.getSB();
        if (sb.length() > 0) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }
}
