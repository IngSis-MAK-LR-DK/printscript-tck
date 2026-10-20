package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new TckInterpreterAdapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new TckFormatterAdapter();
    }

    @Override
    public PrintScriptLinter linter() {
        return new TckLinterAdapter();
    }
}
