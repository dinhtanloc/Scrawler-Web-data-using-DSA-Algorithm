package ueh.model;
public class HtmlData {
    private String url;
    private String rawHtml;
    private String filteredHtml;

    // Constructor
    public HtmlData(String url, String rawHtml) {
        this.url = url;
        this.rawHtml = rawHtml;
    }

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public String getFilteredHtml() {
        return filteredHtml;
    }

    public void setFilteredHtml(String filteredHtml) {
        this.filteredHtml = filteredHtml;
    }
}
