package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Estate {
    String name;
    Weapon weapon;
    int x;
    int y;
    int width;
    int height;

    Map<Square, String> doors = new HashMap<Square, String>();

    /**
     * Estate can have up to one weapon and up to four characters, characters will be stored
     * within a list. Also has dimensions and coordinates
     * -Mathias
     */

    public Estate(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /**
     * Adds a door to the estate
     * Stores it in a map of doors.
     * Doors are not blocked squares, and allow the players to enter estates
     *
     * @param x
     * @param y
     * @param direction
     */

    public void addDoor(int x, int y, String direction) {
        Square door = Board.getSquare(x, y);
        doors.put(door, direction);
        door.setBlocked(false);
    }

    /**
     * Adds a weapon to the estate
     *
     * @param w
     */

    public void addWeapon(Weapon w) {
        weapon = w;
    }

    /**
     * Retrieves the weapon from the estate
     *
     * @return weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Returns the name of the estate
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the estate of a given square
     *
     * @param s
     * @return
     */
    public Estate squarePartOfEstate(Square s) {
        if (s.getX() >= x && s.getX() < x + width && s.getY() >= y && s.getY() < y + height) {
            return this;
        }
        return null;
    }


    /**
     * The top left x position of an estate
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * The top left y position of an estate
     *
     * @return
     */
    public int getY() {
        return y;
    }
}
