package library.model;

/**
 * Represents a book in the library collection.
 */
public class Book {

    private String title;
    private String author;
    private String isbn;
    private int    publicationYear;
    private String genre;

    // ── Constructor ───────────────────────────────────────────────────────
    public Book(String title, String author, String isbn,
                int publicationYear, String genre) {
        this.title           = title;
        this.author          = author;
        this.isbn            = isbn;
        this.publicationYear = publicationYear;
        this.genre           = genre;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public String getTitle()           { return title; }
    public String getAuthor()          { return author; }
    public String getIsbn()            { return isbn; }
    public int    getPublicationYear() { return publicationYear; }
    public String getGenre()           { return genre; }

    // ── Setters ───────────────────────────────────────────────────────────
    public void setTitle(String title)                   { this.title = title; }
    public void setAuthor(String author)                 { this.author = author; }
    public void setIsbn(String isbn)                     { this.isbn = isbn; }
    public void setPublicationYear(int publicationYear)  { this.publicationYear = publicationYear; }
    public void setGenre(String genre)                   { this.genre = genre; }

    // ── Display ───────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "┌─────────────────────────────────────────┐\n" +
            "│  Title  : %-30s│\n" +
            "│  Author : %-30s│\n" +
            "│  ISBN   : %-30s│\n" +
            "│  Year   : %-30s│\n" +
            "│  Genre  : %-30s│\n" +
            "└─────────────────────────────────────────┘",
            title, author, isbn, publicationYear, genre
        );
    }

    /** Compact single-line representation for list views. */
    public String toShortString() {
        return String.format("%-35s | %-20s | %-15s | %4d | %s",
            title, author, isbn, publicationYear, genre);
    }
}
