
/**
 * This class adds items into the game that have a description.
 *
 * @author maximus vancaneghem
 * @version 2022.03.27
 */
public class Item
{
    private String description;
    private int weight;
    
    /**
     * Create a Item with description.
     * @param item description.
     */
    public Item(String newdescription) {
        description = newdescription;
        weight = 0;
    }
    
    /**
     * get the description.
     * @return description.
     */
    public String getDescription() {
        return description;
    }
}
