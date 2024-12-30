package cat.nilcm01;

public class AlbumVariant {
    private String title;
    private String artist;
    private String format;
    private Integer bits;
    private Integer sample;
    private String path;

    public AlbumVariant(String title, String artist, String format, Integer bits, Integer sample, String path) {
        this.title = title;
        this.artist = artist;
        this.format = format;
        this.bits = bits;
        this.sample = sample;
        this.path = path;
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
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public Integer getBits() {
        return bits;
    }
    public void setBits(Integer bits) {
        this.bits = bits;
    }
    public Integer getSample() {
        return sample;
    }
    public void setSample(Integer sample) {
        this.sample = sample;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getDirectoryName() {
        return this.path.split("/")[this.path.split("/").length - 1];
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"format\": \"").append(format).append("\", ");
        sb.append("\"bits\": ").append(bits).append(", ");
        sb.append("\"sample\": ").append(sample).append(", ");
        sb.append("\"path\": \"").append(path).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
