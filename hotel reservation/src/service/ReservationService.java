package service;

import model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author bqlong
 */
public final class ReservationService implements ILuxuryService{
    private static ReservationService instance;

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    final ArrayList<Room> rooms = new ArrayList<>();
    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    public ArrayList<Reservation> getAllReservation() {
        return reservations;
    }

    public void addRoom(IRoom room) {
        String roomNumber = room.getRoomNumber();
        Double roomPrice = room.getRoomPrice();
        RoomType roomType = room.getRoomType();
        boolean isFree = roomPrice == 0.0;
        if (isFree) {
            FreeRoom obRoom = new FreeRoom(roomNumber, roomPrice, roomType);
            rooms.add(obRoom);
        } else {
            Room obRoom = new Room(roomNumber, roomPrice, roomType);
            rooms.add(obRoom);
        }
    }

    public IRoom getRoom(String roomId, String type) {
        IRoom result = null;
        try {
            for (var reversion : reservations) {
                if (reversion.getiRoom().getRoomNumber().equalsIgnoreCase(roomId)) {
                    for (var room : reservations) {
                        if (room.getiRoom().getRoomType().equals(RoomType.valueOf(type))) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(room.getCheckInDate());
                            c.add(Calendar.DATE, 7);
                            String recommendation = formatter.format(c.getTime());
                            System.out.println("THE DATE WHEN THE ROOM AVAILABLE");
                            System.out.println(recommendation);
                            break;
                        }
                    }
                }
            }
            for (var v : rooms) {
                if (roomId.equals(v.getRoomNumber()) &&
                        (v.getRoomType().equals(RoomType.valueOf(type)))) {
                    result = v;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The Type of room has been not not found");
        }
        if(result != null){
            return result;
        }
        return null;
    }

    public Reservation reservation(Customer customer, IRoom room,
                                   Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        boolean isBooked = false;
        for (var res : reservations) {
            if (res.getiRoom().getRoomNumber().equalsIgnoreCase(room.getRoomNumber())) {
                if (res.getCheckInDate().compareTo(checkInDate) <= 0 &&
                        res.getCheckOutDate().compareTo(checkInDate) >= 0) {
                    isBooked = true;
                    break;
                }
            }
        }
        if (!isBooked) {
            System.out.println("BOOKED ROOM SUCCESSFULLY");
            reservations.add(reservation);
            return reservation;
        }
        System.out.println("THIS ROOM HAS ALREADY BOOKED");
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate,
                                       boolean isFree, String type) {
        List<IRoom> result = new ArrayList<>();
        //Give recommendation to customer for booking unavailable room for another day
        if (!reservations.isEmpty()) {
            //ArrayList<Room> allRooms = getAllRooms();
            for (Room room : getAllRooms()) {
                if (room.getRoomType().equals(RoomType.valueOf(type))) {
                    if (isFree) {
                        if (room.isFree()) {
                            reservations.stream()
                                    .filter(x -> !x.getiRoom()
                                            .getRoomNumber()
                                            .equalsIgnoreCase(room.getRoomNumber())).forEach(x -> result.add(room));
                        }
                    } else {
                        if (!room.isFree()) {
                            reservations.stream()
                                    .filter(x -> !x.getiRoom()
                                            .getRoomNumber()
                                            .equalsIgnoreCase(room.getRoomNumber())).forEach(x -> result.add(room));
                        }
                    }
                }
                if (!result.isEmpty()) {
                    return result;
                }
            }
        } else {
            //The room is available, the customer are able to book this room
            if (isFree) {
                instance.getAllRooms().forEach(x -> {
                    if (x.getRoomType().equals(RoomType.valueOf(type)) && x.isFree()) {
                        result.add(x);
                    }
                });
            } else {
                instance.getAllRooms().forEach(x -> {
                    if (x.getRoomType().equals(RoomType.valueOf(type)) && !x.isFree()) {
                        result.add(x);
                    }
                });
            }
            return result;
        }
        return result;
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        for (var reservation : reservations) {
            if (customer == reservation.getCustomer()) {
                return reservations;
            }
        }
        System.out.println("YOU HAVE NOT BOOKED ANY ROOM YET");
        return Collections.emptyList();
    }

    public List<List<IRoom>> roomBySchedule(Date checkInDate) {
        List<List<IRoom>> result = new ArrayList<>();
        //Get all rooms
        ArrayList<IRoom> listRoomsAvailable = new ArrayList<>();
        ArrayList<IRoom> listRoomsDuplicate = new ArrayList<>();

        if(ReservationService.getInstance().getAllReservation().isEmpty() &&
                !ReservationService.getInstance().getAllRooms().isEmpty()){
            System.out.println("THERE ARE ALL ROOMS ARE AVAILABLE");
            listRoomsAvailable.addAll(ReservationService.getInstance().getAllRooms());
            result.add(listRoomsAvailable);
            return result;
        }

        //GET ROOM AVAILABLE
        ArrayList<Reservation> allReservation = ReservationService.getInstance().getAllReservation();
        ArrayList<Room> allRooms = ReservationService.getInstance().getAllRooms();

        for (var room : allRooms) {
            boolean isBelongs = false;
            for (var reservation : allReservation) {
                if(room.getRoomNumber().equalsIgnoreCase(reservation.getiRoom().getRoomNumber())){
                    isBelongs = true;
                    break;
                }
            }
            if(!isBelongs) listRoomsAvailable.add(room);
        }

        for (var reservation : allReservation) {
            if (reservation.getCheckInDate().compareTo(checkInDate) <= 0 &&
                    reservation.getCheckOutDate().compareTo(checkInDate) >= 0) {
                listRoomsDuplicate.add(reservation.getiRoom());
            }
        }

        result.add(listRoomsAvailable);
        result.add(listRoomsDuplicate);

        return result;
    }

    public ArrayList<String> updateRecommendationDate(Date checkIn, Date checkOut) {
        int dayReservation = Math.abs(checkIn.compareTo(checkOut))+1;
        ArrayList<String> listRecommendationDate = new ArrayList<>();

        ReservationService.getInstance().getAllReservation().forEach(x ->{
            Date checkOutDate = x.getCheckOutDate();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(checkOutDate);
            c.add(Calendar.DATE, 7);

            String recommendationCheckIn = formatter.format(c.getTime());

            c.add(Calendar.DATE, dayReservation);
            String recommendationCheckOut = formatter.format(c.getTime());

            listRecommendationDate.add(recommendationCheckIn);
            listRecommendationDate.add(recommendationCheckOut);
        });

        return listRecommendationDate;
    }
    @Override
    public void addOptional() {
        ILuxuryService.super.addOptional();
    }
}
