package model;

/**
 * @author bqlong
 */

public interface IRoom {
    String getRoomNumber();

    Double getRoomPrice();

    RoomType getRoomType();

    boolean isFree();
}
