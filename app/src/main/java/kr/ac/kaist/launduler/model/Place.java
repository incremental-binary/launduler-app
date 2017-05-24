package kr.ac.kaist.launduler.model;

/**
 * A place where washing machines are installed.
 */
public class Place {
    private long id;
    private String name;

    public Place(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
