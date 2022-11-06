import api.HotelResource;
import model.*;
import service.ReservationService;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author bqlong
 */

public class MainMenu {
    public static void main(String[] args) throws ParseException {
        System.out.println("\n" + "WELCOME HOTEL RESOURCE");
        System.out.println("1. Find and reverse a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");

        Scanner sc = new Scanner(System.in);
        System.out.println("Please select a number for the menu option");

        int option = sc.nextInt();
        while (1 <= option && option <= 5) {
            switch (option) {
                case 1:
                    System.out.println("DAY CHECKING MUST BE LARGER THAN OR EQUAL DAY CHECKOUT");
                    System.out.println("Enter CheckIn Date dd/MM/yyyy example 29/10/2022");
                    String dateCheckIn = sc.next();
                    System.out.println("Enter CheckOut Date dd/MM/yyyy example 30/10/2022");
                    String dateCheckOut = sc.next();

                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateCheckIn);
                    Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateCheckOut);

                    List<List<IRoom>> availableRoom_bySchedule = HotelResource.getAvailableRoom_BySchedule(date1);

                    String bookRecommendationDate;
                    Date checkIn, checkOut;

                    //CASE HAVE AVAILABLE ROOMS AND ROOMS DUPLICATE WITH DATE
                    if (availableRoom_bySchedule.size() == 2 && availableRoom_bySchedule.get(0).size() != 0) {
                        System.out.println("THERE ARE ROOM(S) AVAILABLE IN YOUR DATE");
                        System.out.println(availableRoom_bySchedule.get(0) + "\n");


                        String roomType, roomNumber;
                        while (true) {
                            System.out.println("Enter room type 1 for single or 2 for double bed ");
                            int type = sc.nextInt();
                            roomType = type == 1 ? "SINGLE" : "DOUBLE";

                            System.out.println("Enter a room number ");
                            roomNumber = sc.next();
                            var checkRoom = HotelResource.getRoom(roomNumber, roomType);
                            if (null != checkRoom) break;
                        }

                        System.out.println("Enter the price of room ");
                        double isFree = sc.nextDouble();


                        System.out.println("Would you like to book a room? y/n");
                        String book = sc.next();

                        while (!"n".equalsIgnoreCase(book) && !"y".equalsIgnoreCase(book)) {
                            System.out.println("Would you like to book a room? y/n");
                            book = sc.next();
                        }

                        if ("n".equalsIgnoreCase(book)) break;

                        String account;
                        if ("y".equalsIgnoreCase(book)) {
                            String acc;
                            do {
                                System.out.println("Do you have an account with us? y/n");
                                acc = sc.next();
                            } while (!"n".equalsIgnoreCase(acc) && !"y".equalsIgnoreCase(acc));

                            if ("n".equalsIgnoreCase(acc)) break;

                            System.out.println("Enter Email format name@domain.com");
                            account = sc.next();

                            Customer customer = HotelResource.getCustomer(account);
                            while (null == customer) {
                                System.out.println("The account has not been found. Please enter email again");
                                account = sc.next();
                                customer = HotelResource.getCustomer(account);
                            }
                            IRoom room;
                            if (isFree == 0.0) {
                                room = new FreeRoom(roomNumber, isFree, RoomType.valueOf(roomType));
                            } else {
                                room = new Room(roomNumber, isFree, RoomType.valueOf(roomType));
                            }
                            HotelResource.bookARoom(account, room, date1, date2);
                        }

                    } else if (availableRoom_bySchedule.size() == 2 && availableRoom_bySchedule.get(0).size() == 0) {
                        System.out.println("THERE ARE ROOMS(S) UNAVAILABLE IN YOUR DATE");
                        System.out.println(availableRoom_bySchedule.get(1));
                        String chocie;
                        do {
                            System.out.println("WOULD YOU LIKE TO BOOK THIS ROOM IN OUR RECOMMENDATION DATE y/n");
                            chocie = sc.next();
                        } while (!"y".equalsIgnoreCase(chocie) && !"n".equalsIgnoreCase(chocie));

                        if ("n".equalsIgnoreCase(chocie)) break;

                        ArrayList<String> recommendationDate = HotelResource.updateRecommendationDate(date1, date2);
                        System.out.println("OUR RECOMMEND FOR YOU");
                        System.out.println(recommendationDate);
                        System.out.println("Do you still want to book this room at recommendation date y/n");
                        bookRecommendationDate = sc.next();
                        while (!"y".equalsIgnoreCase(bookRecommendationDate) &&
                                !"n".equalsIgnoreCase(bookRecommendationDate)) {
                            System.out.println("Do you still want to book this room at recommendation date y/n");
                            bookRecommendationDate = sc.next();
                        }
                        if ("n".equalsIgnoreCase(bookRecommendationDate)) break;

                        //RECOMMENDATION DATE
                        checkIn = new SimpleDateFormat("dd-MM-yyyy").parse(recommendationDate.get(0));
                        checkOut = new SimpleDateFormat("dd-MM-yyyy").parse(recommendationDate.get(1));
                        System.out.println("RECOMMENDATION DATE FOR ROOM");
                        System.out.println("CheckIn Date: " + checkIn + " CheckOut Date: " + checkOut);

                        //CONFIRM ONCE AGAIN THE CUSTOMER STILL WANT TO BOOK IN THAT DATE
                        System.out.println("Confirm that you still want to book room in that date y/n");
                        bookRecommendationDate = sc.next();
                        while (!"y".equalsIgnoreCase(bookRecommendationDate) &&
                                !"n".equalsIgnoreCase(bookRecommendationDate)) {
                            System.out.println("Confirm that you still want to book room in that date y/n");
                            bookRecommendationDate = sc.next();
                        }
                        if ("n".equalsIgnoreCase(bookRecommendationDate)) break;

                        String roomTypeRecommendation, roomNumberRecommendation;
                        //RESERVATION WITH RECOMMENDATION DATE
                        System.out.println("Enter room type 1 for single or 2 for double bed ");
                        int type = sc.nextInt();
                        roomTypeRecommendation = type == 1 ? "SINGLE" : "DOUBLE";

                        System.out.println("Enter a room number ");
                        roomNumberRecommendation = sc.next();

                        System.out.println("Enter the price of room ");
                        double isFree = sc.nextDouble();

                        System.out.println("Would you like to book a room? y/n");
                        String book = sc.next();
                        while (!"n".equalsIgnoreCase(book) && !"y".equalsIgnoreCase(book)) {
                            System.out.println("Would you like to book a room? y/n");
                            book = sc.next();
                        }

                        if ("n".equalsIgnoreCase(book)) break;

                        String account;
                        if ("y".equalsIgnoreCase(book)) {
                            System.out.println("Do you have an account with us? y/n");
                            String acc = sc.next();

                            while (!"n".equalsIgnoreCase(acc) && !"y".equalsIgnoreCase(acc)) {
                                System.out.println("Do you have an account with us? y/n");
                                acc = sc.next();
                            }

                            if ("n".equalsIgnoreCase(acc)) break;

                            System.out.println("Enter Email format name@domain.com");
                            account = sc.next();

                            Customer customer = HotelResource.getCustomer(account);
                            while (null == customer) {
                                System.out.println("The account has not been found. Please enter email again");
                                account = sc.next();
                                customer = HotelResource.getCustomer(account);
                            }

                            IRoom room;
                            if (isFree == 0.0) {
                                room = new FreeRoom(roomNumberRecommendation, isFree, RoomType.valueOf(roomTypeRecommendation));
                            } else {
                                room = new Room(roomNumberRecommendation, isFree, RoomType.valueOf(roomTypeRecommendation));
                            }
                            HotelResource.bookARoom(account, room, checkIn, checkOut);
                        }
                    } else if (availableRoom_bySchedule.size() == 1) {
                        System.out.println(availableRoom_bySchedule.get(0));
                        String roomType, roomNumber;
                        while (true) {
                            System.out.println("Enter room type 1 for single or 2 for double bed ");
                            int type = sc.nextInt();
                            roomType = type == 1 ? "SINGLE" : "DOUBLE";

                            System.out.println("Enter a room number ");
                            roomNumber = sc.next();
                            var checkRoom = HotelResource.getRoom(roomNumber, roomType);
                            if (null != checkRoom) break;
                        }

                        System.out.println("Enter the price of room ");
                        double isFree = sc.nextDouble();


                        System.out.println("Would you like to book a room? y/n");
                        String book = sc.next();

                        while (!"n".equalsIgnoreCase(book) && !"y".equalsIgnoreCase(book)) {
                            System.out.println("Would you like to book a room? y/n");
                            book = sc.next();
                        }

                        if ("n".equalsIgnoreCase(book)) break;

                        String account;
                        if ("y".equalsIgnoreCase(book)) {
                            System.out.println("Do you have an account with us? y/n");
                            String acc = sc.next();

                            while (!"n".equalsIgnoreCase(acc) && !"y".equalsIgnoreCase(acc)) {
                                System.out.println("Do you have an account with us? y/n");
                                acc = sc.next();
                            }

                            if ("n".equalsIgnoreCase(acc)) break;

                            System.out.println("Enter Email format name@domain.com");
                            account = sc.next();

                            Customer customer = HotelResource.getCustomer(account);
                            while (null == customer) {
                                System.out.println("The account has not been found. Please enter email again");
                                account = sc.next();
                                customer = HotelResource.getCustomer(account);
                            }
                            IRoom room;
                            if (isFree == 0.0) {
                                room = new FreeRoom(roomNumber, isFree, RoomType.valueOf(roomType));
                            } else {
                                room = new Room(roomNumber, isFree, RoomType.valueOf(roomType));
                            }
                            HotelResource.bookARoom(account, room, date1, date2);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Please enter email to find customer: ");
                    String email = sc.next();
                    System.out.println(HotelResource.getCustomersReservations(email));
                    break;
                case 3:
                    System.out.println("Please enter email to create account: ");
                    String ema = sc.next();

                    System.out.println("Please enter firstname to create account: ");
                    String firstName = sc.next();

                    System.out.println("Please enter lastname to create account: ");
                    String lastName = sc.next();

                    HotelResource.createACustomer(ema, firstName, lastName);
                    break;
                case 4:
                    AdminMenu.execute();
                    break;
            }
            if (option == 5) break;
            System.out.println("\n" + "WELCOME HOTEL RESOURCE");
            System.out.println("1. Find and reverse a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");

            System.out.println("Please select a number for the menu option");
            option = sc.nextInt();
        }
    }
}
