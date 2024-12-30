package cat.nilcm01;

import cat.nilcm01.utils.DirectoryManagement;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private String path;
    private List<Artist> artists;
    private Boolean status = false;

    public Library(String path, List<Artist> artists) {
        this.path = path;
        this.artists = artists;
        this.status = true;
    }

    public Library(String path) {
        this.path = path;
        this.status = update(path);
    }

    private Boolean update(String path) {
        this.path = path;

        // Populate artists with all distinct directory names in path
        List<String> directories;
        try {
            directories = DirectoryManagement.getDirectoryNames(path);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Error while getting directories from path: " + path);
            this.artists = null;
            return false;
        }

        // Initialize artists list
        this.artists = new ArrayList<>();

        for (String directory: directories) {
            Artist artist = new Artist(directory, path + "/" + directory);
            this.artists.add(artist);
        }

        return true;
    }

    private void reconnect() {
        if (path == null) return;
        this.status = update(path);
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public List<Artist> getArtists() {
        return artists;
    }
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<String> getArtistsNames() {
        if (artists == null) return null;
        List<String> names = new ArrayList<>();
        for (Artist artist: artists) {
            names.add(artist.getName());
        }
        return names;
    }

    public List<String> getArtistsAlbumsNames(String artistName) {
        if (artists == null) return null;
        for (Artist artist: artists) {
            if (artist.getName().equals(artistName)) {
                return artist.getAlbumsNames();
            }
        }
        return null;
    }

    public Artist getArtist(String artistName) {
        if (artists == null) return null;
        for (Artist artist: artists) {
            if (artist.getName().equals(artistName)) {
                return artist;
            }
        }
        return null;
    }
    public Album getAlbum(String artistName, String albumName) {
        Artist artist = getArtist(artistName);
        if (artist == null) return null;
        return artist.getAlbum(albumName);
    }

    public AlbumVariant getAlbumVariant(String artistName, String albumName, String format, Integer bits, Integer sample) {
        Album album = getAlbum(artistName, albumName);
        if (album == null) return null;
        return album.getVariant(format, bits, sample);
    }

    public Boolean isConnected() {
        if (DirectoryManagement.isDirectoryConnected(path)) {
            return true;
        } else {
            reconnect();
            return status;
        }
    }

    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"path\": \"").append(path).append("\",\n");
        json.append("  \"artists\": [\n");
        for (int i = 0; i < artists.size(); i++) {
            json.append(artists.get(i).toJSON());
            if (i < artists.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }
}
