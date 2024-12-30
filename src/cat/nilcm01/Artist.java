package cat.nilcm01;

import cat.nilcm01.utils.DirectoryManagement;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private String name;
    private List<Album> albums;
    private String path;

    public Artist(String name, List<Album> albums, String path) {
        this.name = name;
        this.albums = albums;
        this.path = path;
    }

    public Artist(String name, String path) {
        this.name = name;
        this.path = path;

        // Populate albums with all distinct directory names in path
        // Directory name format: <album title> [<format>] [<bits>-<sample>]
        // Example: "Master of Puppets [FLAC] [16-44,1]"
        // Only the album title is considered, the rest is ignored for now
        List<String> directories = null;
        try {
            directories = DirectoryManagement.getDirectoryNames(path);
        } catch (Exception e) {
            e.printStackTrace();
            this.albums = null;
            return;
        }

        // Initialize albums list
        this.albums = new ArrayList<>();

        for (String directory: directories) {
            // Check if there already is an album with the same title
            boolean found = false;
            for (Album album: this.albums) {
                if (directory.contains(album.getTitle())) {
                    found = true;
                    break;
                }
            }

            if (found) continue;

            // From the directory name, extract the album title
            // Example: "Master of Puppets [FLAC] [16-44,1]"
            String[] parts = directory.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 2; i++) {
                sb.append(parts[i]);
                if (i < parts.length - 3) sb.append(" ");
            }
            Album album = new Album(sb.toString(), name, path);
            this.albums.add(album);
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Album> getAlbums() {
        return albums;
    }
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getAlbumsNames() {
        if (albums == null) return null;
        List<String> names = new ArrayList<>();
        for (Album album: albums) {
            names.add(album.getTitle());
        }
        return names;
    }

    public Album getAlbum(String albumName) {
        if (albums == null) return null;
        for (Album album: albums) {
            if (album.getTitle().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(name).append("\", ");
        sb.append("\"albums\": [");
        for (Album album: albums) {
            sb.append(album.toJSON()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
