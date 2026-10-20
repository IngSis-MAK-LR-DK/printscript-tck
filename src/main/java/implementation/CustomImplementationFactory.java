package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

public class CustomImplementationFactory implements implementation.PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new implementation.TckInterpreterAdapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new implementation.TckFormatterAdapter();
    }

    @Override
    public PrintScriptLinter linter() {
        return new implementation.TckLinterAdapter();
    }
}
