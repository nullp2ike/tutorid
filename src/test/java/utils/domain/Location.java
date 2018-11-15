package utils.domain;

public class Location {

    private final int id;
    private final String type;
    /*
        TODO in the future add also other properties of location
     */

    public Location(final int id, final String type){
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }
}
