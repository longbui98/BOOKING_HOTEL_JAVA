package api;

import model.*;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author bqlong
 */

public class AdminResource {
    public static Customer getCustomer(String email) {
        CustomerService instance = CustomerService.getInstance();
        Customer customer = instance.getCustomer(email);
        return customer;
    }

    public static void addRoom(List<IRoom> rooms) {
        ReservationService instance = ReservationService.getInstance();

        String newRoom = rooms.get(0).getRoomNumber();

        Optional<Room> any = instance.getAllRooms()
                .stream()
                .filter(x -> x.getRoomNumber().equalsIgnoreCase(newRoom))
                .findAny();

        if (!any.isEmpty()) {
            System.out.println("THIS ROOM HAS ALREADY EXIST");
            return;
        }
        for(var room : rooms){
            instance.addRoom(room);
        }
        System.out.println("CREATE ROOM SUCCESSFULLY");
    }

    public static Collection<IRoom> getAllRooms() {
        ReservationService instance = ReservationService.getInstance();

        return new ArrayList<>(instance.getAllRooms());
    }

    public static Collection<Customer> getAllCustomers() {
        CustomerService instance = CustomerService.getInstance();
        return instance.getAllCustomers();
    }

    public static void displayAllReservations() {
        ReservationService instance = ReservationService.getInstance();
        for(var reservation : instance.getAllReservation()){
            System.out.println(reservation);
        }
    }
}
