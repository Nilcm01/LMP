package cat.nilcm01.utils;

import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryManagement {

    public static List<String> getDirectoryNames(String path) throws IOException {
        try (var paths = Files.list(Paths.get(path))) {
            return paths
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    public static Boolean isDirectoryConnected(String path) {
        try {
            Files.list(Paths.get(path)).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public interface ProgressListener {
        void onProgress(int progress);
    }

    public static Boolean copyDirectory(String source, String destination, ProgressListener listener) {
        try {
            // Create destination directory if it does not exist
            Path destinationPath = Paths.get(destination);
            if (!Files.exists(destinationPath)) {
                Files.createDirectories(destinationPath);
            }

            Path start = Paths.get(source);
            List<Path> pathsToCopy = Files.walk(start).toList();
            long totalFiles = pathsToCopy.size();

            long[] copiedFiles = {0};

            for (Path sourcePath : pathsToCopy) {
                Path targetPath = destinationPath.resolve(start.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    copiedFiles[0]++;
                    int progress = (int) ((copiedFiles[0] * 100) / totalFiles);
                    listener.onProgress(progress);
                }
            }
            // Ensure 100% progress is reported
            listener.onProgress(100);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isDirectoryEmpty(String path) {
        try {
            return Files.list(Paths.get(path)).findAny().isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deleteDirectory(String path, ProgressListener listener) {
        try {
            List<Path> pathsToDelete = Files.walk(Paths.get(path))
                    .sorted((a, b) -> b.toString().length() - a.toString().length()).toList();
            long totalFiles = pathsToDelete.size();
            long[] deletedFiles = {0};

            for (Path p : pathsToDelete) {
                Files.delete(p);
                deletedFiles[0]++;
                int progress = (int) ((deletedFiles[0] * 100) / totalFiles);
                listener.onProgress(progress);
            }
            // Ensure 100% progress is reported - Not needed, commented just in case
            // listener.onProgress(100);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
