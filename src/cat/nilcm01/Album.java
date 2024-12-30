package cat.nilcm01;

import cat.nilcm01.utils.DirectoryManagement;

import java.util.ArrayList;
import java.util.List;

import static cat.nilcm01.utils.DirectoryManagement.getDirectoryNames;

public class Album {
    private String title;
    private String artist;
    private List<AlbumVariant> variants;
    private String artistPath;

    public Album(String title, String artist, List<AlbumVariant> variants, String artistPath) {
        this.title = title;
        this.artist = artist;
        this.variants = variants;
        this.artistPath = artistPath;
    }

    public Album(String title, String artist, String artistPath) {
        // Populate variants with all directories inside artistPath that match the album title
        // Directory name format: <album title> [<format>] [<bits>-<sample>]
        // Example: "Master of Puppets [FLAC] [16-44,1]"
        this.title = title;
        this.artist = artist;
        this.artistPath = artistPath;

        // Get all directories inside artistPath
        List<String> directories = null;
        try {
            directories = DirectoryManagement.getDirectoryNames(artistPath);
        } catch (Exception e) {
            e.printStackTrace();
            this.variants = null;
            return;
        }

        // Initialize variants list
        this.variants = new ArrayList<>();

        // For each directory, check if it matches the album title format
        for (String directory: directories) {
            if (directory.startsWith(title)) {
                // If it does, create a new AlbumVariant and add it to the variants list
                // Example: "Master of Puppets [FLAC] [16-44,1]"
                String[] parts = directory.split(" ");
                String format = parts[parts.length - 2].substring(1, parts[parts.length - 2].length() - 1);
                // Example: "16-44,1", "24-96", etc.
                String[] bitsSample = parts[parts.length - 1].substring(1, parts[parts.length - 1].length() - 1).split("-");
                // Bits: 16, 24, etc.
                Integer bits = Integer.parseInt(bitsSample[0]);
                // Sample: 44,1, 96, etc.
                // If the sample rate is not an integer, the decimal part will be ignored
                Integer sample = Integer.parseInt(bitsSample[1].split(",")[0]);
                // Artist path
                String path = artistPath + "/" + directory;
                AlbumVariant variant = new AlbumVariant(title, artist, format, bits, sample, path);
                this.variants.add(variant);
            }
        }
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public List<AlbumVariant> getVariants() {
        return variants;
    }
    public void setVariants(List<AlbumVariant> variants) {
        this.variants = variants;
    }
    public String getArtistPath() {
        return artistPath;
    }
    public void setArtistPath(String artistPath) {
        this.artistPath = artistPath;
    }

    public AlbumVariant getVariant(String format, Integer bits, Integer sample) {
        for (AlbumVariant variant: variants) {
            if (variant.getFormat().equals(format) && variant.getBits().equals(bits) && variant.getSample().equals(sample)) {
                return variant;
            }
        }
        return null;
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"title\": \"").append(title).append("\", ");
        //sb.append("\"artist\": \"").append(artist).append("\", ");
        sb.append("\"artistPath\": \"").append(artistPath).append("\", ");
        sb.append("\"variants\": [");
        for (int i = 0; i < variants.size(); i++) {
            sb.append(variants.get(i).toJSON());
            if (i < variants.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
