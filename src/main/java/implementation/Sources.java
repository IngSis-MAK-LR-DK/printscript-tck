package implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Spools an {@link InputStream} to a temp file */
final class Sources {

    private Sources() {}

    static Path spoolToTempFile(InputStream in) {
        try (in) {
            Path tempFile = Files.createTempFile("printscript-tck-", ".ps");
            tempFile.toFile().deleteOnExit();
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
