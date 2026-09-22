package implementation;

import edu.austral.ingsis.printscript.analyzer.AnalysisFinding;
import edu.austral.ingsis.printscript.analyzer.AnalyzerConfig;
import edu.austral.ingsis.printscript.analyzer.PrintScriptAnalyzer;
import edu.austral.ingsis.printscript.common.OperatorDefinition;
import edu.austral.ingsis.printscript.common.PrintScriptException;
import edu.austral.ingsis.printscript.common.Version;
import edu.austral.ingsis.printscript.common.ast.Statement;
import edu.austral.ingsis.printscript.lexer.FilePositionalSource;
import edu.austral.ingsis.printscript.lexer.PrintScriptLexer;
import edu.austral.ingsis.printscript.parser.PrintScriptParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

/** Adapts our style analyzer to the TCK's {@link PrintScriptLinter} contract. */
public final class TckLinterAdapter implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Path tempFile = Sources.spoolToTempFile(src);
        try {
            Version parsedVersion = Version.fromLabel(version);
            Set<OperatorDefinition> noPlugins = Set.of();

            try (FilePositionalSource source = new FilePositionalSource(tempFile)) {
                var lexer = new PrintScriptLexer(noPlugins);
                var parser = new PrintScriptParser(noPlugins, parsedVersion);
                Iterator<Statement> statements = parser.parse(lexer.tokenize(source));

                AnalyzerConfig analyzerConfig = TckConfig.analyzerConfigFrom(config);
                List<AnalysisFinding> findings =
                        new PrintScriptAnalyzer().analyze(statements, analyzerConfig);
                findings.forEach(finding -> handler.reportError(finding.message()));
            }
        } catch (PrintScriptException e) {
            handler.reportError(TckInterpreterAdapter.describe(e));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            TckInterpreterAdapter.deleteQuietly(tempFile);
        }
    }
}
