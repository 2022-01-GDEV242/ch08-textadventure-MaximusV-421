/**
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author Maximus Vancaneghem
 * @version 2022.03.25
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    Room the_hub, control_room, raider_camp, boneyard, offices, military_base;
    Room water_station, broken_hills, the_den, cathedral, junktown, vault_303;
    Room hot_springs, shimmering_cliffs, toxic_caves;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }
    
    public static void main(String[] args) {
        Game mygame = new Game();
        mygame.play();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        the_hub = new Room("Main starting point of Nuka World");
        control_room = new Room("Abondoned control room that no longer functions");
        offices = new Room("Old offices that have been infected by rats");
        boneyard = new Room("A graveyard of misery and malice");
        raider_camp = new Room("Camp full of heartless raiders");
        water_station = new Room("Long forgotten water station");
        broken_hills = new Room("A place of both hope and defeat");
        vault_303 = new Room("Vault from the war that has been infiltrated by raiders");
        hot_springs = new Room("Thermic energy grounds with lush life");
        shimmering_cliffs = new Room("Steep cliffs of rock");
        cathedral = new Room("Meeting place for religous folk");
        military_base = new Room("Abondoned military base filled with arms and ammo");
        the_den = new Room("Owned by the machine gunners clan");
        toxic_caves = new Room("Crawling with super mutants and other wild specimen");
        junktown = new Room("A place of civilization and trading");
        
        // initialise room exits
        the_hub.setExit("north", control_room);
        the_hub.setExit("south", offices);
        the_hub.setExit("east", water_station);
        the_hub.setExit("west", boneyard);
        
        control_room.setExit("west", raider_camp);
        control_room.setExit("south", the_hub);
        
        offices.setExit("north", the_hub);
        offices.setExit("south", military_base);
        offices.setExit("east", broken_hills);
        
        water_station.setExit("south", broken_hills);
        water_station.setExit("east", hot_springs);
        water_station.setExit("west", the_hub);
        
        boneyard.setExit("north", raider_camp);
        boneyard.setExit("east", the_hub);
        
        raider_camp.setExit("south", boneyard);
        raider_camp.setExit("east", control_room);
        
        broken_hills.setExit("north", water_station);
        broken_hills.setExit("south", the_den);
        broken_hills.setExit("east", shimmering_cliffs);
        broken_hills.setExit("west", offices);
        
        shimmering_cliffs.setExit("north", hot_springs);
        shimmering_cliffs.setExit("south", toxic_caves);
        shimmering_cliffs.setExit("west", broken_hills);
        
        hot_springs.setExit("north", vault_303);
        hot_springs.setExit("south", shimmering_cliffs);
        hot_springs.setExit("west", water_station);
        
        vault_303.setExit("south", hot_springs);
        
        military_base.setExit("north", offices);
        military_base.setExit("east", the_den);
        military_base.setExit("west", cathedral);
        
        cathedral.setExit("south", junktown);
        cathedral.setExit("east", military_base);
        
        junktown.setExit("north", cathedral);
        
        the_den.setExit("north", broken_hills);
        the_den.setExit("east", toxic_caves);
        the_den.setExit("west", military_base);
        
        toxic_caves.setExit("north", shimmering_cliffs);
        toxic_caves.setExit("west", the_den);
    
        currentRoom = the_hub;  // start game at the hub
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case EAT:
                eat();
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Look at something.
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Eat and fill hunger, this method will matter in a later update.
     */
    private void eat()
    {
        System.out.println("You have eaten and are no longer hungry.");
    }
}
