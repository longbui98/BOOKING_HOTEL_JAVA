package model;

import java.util.Objects;

/**
 * @author bqlong
 */

public class FreeRoom extends Room{
    private String roomNumber;
    private Double price;
    private RoomType enumeration;

    public FreeRoom(String roomNumber, Double price, RoomType enumeration) {
        super(roomNumber, price, enumeration);
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    public Double getPrice() {
        return price;
    }

    public RoomType getEnumeration() {
        return enumeration;
    }

    @Override
    public String toString() {
        return "FreeRoom{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", enumeration=" + enumeration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FreeRoom freeRoom = (FreeRoom) o;
        return roomNumber.equals(freeRoom.roomNumber) && price.equals(freeRoom.price) && enumeration == freeRoom.enumeration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roomNumber, price, enumeration);
    }
}
