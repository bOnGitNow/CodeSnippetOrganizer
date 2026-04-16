package model;

public class Snippet {
    private int id;
    private String title;
    private String language;
    private String code;
    private String tags;

    public Snippet(String title, String language, String code, String tags) {
        this.title = title; this.language = language;
        this.code = code;   this.tags = tags;
    }

    public Snippet(int id, String title, String language, String code, String tags) {
        this.id = id; this.title = title; this.language = language;
        this.code = code; this.tags = tags;
    }

    public int getId()          { return id; }
    public String getTitle()    { return title; }
    public String getLanguage() { return language; }
    public String getCode()     { return code; }
    public String getTags()     { return tags; }

    public void setTitle(String t)    { this.title = t; }
    public void setLanguage(String l) { this.language = l; }
    public void setCode(String c)     { this.code = c; }
    public void setTags(String t)     { this.tags = t; }

    @Override
    public String toString() {
        return title; // only title shown
    }
}