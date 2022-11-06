package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bqlong
 */
public class HotelResource {
    public static Customer getCustomer(String email) {
        CustomerService instance = CustomerService.getInstance();
        return instance.getCustomer(email);
    }
    public static void createACustomer(String email, String firstName, String lastName) {
        CustomerService instance = CustomerService.getInstance();
        instance.addCustomer(email, firstName, lastName);
    }
    //Get A Room and Type
    public static IRoom getRoom(String roomNumber, String type) {
        ReservationService instance = ReservationService.getInstance();
        return instance.getRoom(roomNumber, type);
    }

    public static List<List<IRoom>> getAvailableRoom_BySchedule(Date checkInDate) {
        ReservationService instance = ReservationService.getInstance();
        return instance.roomBySchedule(checkInDate);
    }

    //BookRoom and Type follow requirement from customer
    public static Reservation bookARoom(String customerEmail, IRoom iRoom,
                                        Date checkInDate, Date checkOutDate) {
        ReservationService instance = ReservationService.getInstance();
        CustomerService instanceCustomer = CustomerService.getInstance();
        Customer customer = instanceCustomer.getCustomer(customerEmail);
        return instance.reservation(customer, iRoom, checkInDate, checkOutDate);
    }
    public static Collection<Reservation> getCustomersReservations(String customerEmail) {
        ReservationService instance = ReservationService.getInstance();
        CustomerService instanceCustomer = CustomerService.getInstance();
        Customer customer = instanceCustomer.getCustomer(customerEmail);
        return instance.getCustomerReservation(customer);
    }
    //Find a Room has free or not and also has type of room.
    public static Collection<IRoom> findARoom(Date checkIn, Date checkOut, boolean isFree, String type) {
        ReservationService instance = ReservationService.getInstance();
        return instance.findRooms(checkIn, checkOut, isFree, type);
    }

    public static ArrayList<String> updateRecommendationDate(Date checkIn, Date checkOut) {
        ReservationService instance = ReservationService.getInstance();
        return instance.updateRecommendationDate(checkIn, checkOut);
    }
}
