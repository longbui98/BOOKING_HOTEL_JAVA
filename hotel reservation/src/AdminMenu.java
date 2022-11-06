import api.AdminResource;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author bqlong
 */
public class AdminMenu {
    public static void execute() {
        System.out.println("\n" + "ADMIN PAGE");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Add a Test Data");
        System.out.println("6. Back to Main Menu");

        Scanner sc = new Scanner(System.in);
        System.out.println("Please select a number for the menu option");
        int option = sc.nextInt();
        while (1 <= option && option <= 6) {
            switch (option) {
                case 1:
                    System.out.println(AdminResource.getAllCustomers());
                    break;
                case 2:
                    System.out.println(AdminResource.getAllRooms());
                    break;
                case 3:
                    AdminResource.displayAllReservations();
                    break;
                case 4:
                    while (true) {
                        ArrayList<IRoom> iRooms = new ArrayList<>();
                        System.out.println("Enter room number");
                        String roomNumber = sc.next();

                        System.out.println("Enter price per night");
                        Double price = sc.nextDouble();

                        System.out.println("Enter room type 1 for single or 2 for double bed ");
                        int type = sc.nextInt();
                        RoomType roomType = type == 1 ? RoomType.SINGLE : RoomType.DOUBLE;

                        boolean isFree = (price == 0.0);

                        if (isFree) {
                            FreeRoom freeRoom = new FreeRoom(roomNumber, price, roomType);
                            iRooms.add(freeRoom);
                        } else {
                            Room room = new Room(roomNumber, price, roomType);
                            iRooms.add(room);
                        }
                        AdminResource.addRoom(iRooms);
                        System.out.println("Would you like to add another room y/n");
                        String optionContinue = sc.next();
                        while (!"y".equalsIgnoreCase(optionContinue) || !"n".equalsIgnoreCase(optionContinue)) {
                            if ("y".equalsIgnoreCase(optionContinue) || "n".equalsIgnoreCase(optionContinue)) {
                                break;
                            }
                            optionContinue = sc.next();
                        }

                        if ("n".equalsIgnoreCase(optionContinue))break;
                    }
                    break;
                case 5:
                    while (true) {
                        System.out.println("Enter Customer data");
                        System.out.println("Enter firstname of customer");
                        String firstName = sc.next();

                        System.out.println("Enter lastname of customer");
                        String lastName = sc.next();

                        System.out.println("Enter email of customer");
                        String email = sc.next();
                        Customer customer = new Customer(firstName, lastName, email);

                        System.out.println("Enter Room data");
                        String roomNumber = sc.next();
                        double price = sc.nextDouble();

                        List<IRoom> roomList = new ArrayList<>();
                        while(true){
                            System.out.println("Enter room type 1 for single or 2 for double bed ");
                            int type = sc.nextInt();

                            RoomType roomType = type == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
                            if (price != 0.0) {
                                Room room = new Room(roomNumber, price, roomType);
                                roomList.add(room);
                            } else {
                                FreeRoom freeRoom = new FreeRoom(roomNumber, 0.0, roomType);
                                roomList.add(freeRoom);
                            }
                            AdminResource.addRoom(roomList);
                            System.out.println("Would you like to add another room test");
                            String testRoom = sc.next();
                            while (!"y".equalsIgnoreCase(testRoom) && !"n".equalsIgnoreCase(testRoom)) {
                                System.out.println("Would you like to add another room test");
                                testRoom = sc.next();
                            }
                            if("n".equalsIgnoreCase(testRoom)) break;
                        }
                        System.out.println("Would you like to add another data test");
                        String dataTest = sc.next();
                        while (!"y".equalsIgnoreCase(dataTest) && !"n".equalsIgnoreCase(dataTest)) {
                            System.out.println("Would you like to add another data test");
                            dataTest = sc.next();
                        }
                        if("n".equalsIgnoreCase(dataTest)) break;
                    }
            }
            if (option == 6) break;
            System.out.println("\n"+"ADMIN PAGE");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Add a Test Data");
            System.out.println("6. Back to Main Menu");
            System.out.println("Please select a number for the menu option");
            option = sc.nextInt();
        }
    }
}
