package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static ReservationService reservationService = null;

    private final Map<String, IRoom> roomMap = new HashMap<>();
    private final Set<Reservation> reservations = new HashSet<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public void addRoom(IRoom room) {
        roomMap.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return roomMap.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>(roomMap.values());

        for (Reservation reservation : reservations) {
            if (
                    checkInDate.before(reservation.getCheckOutDate()) &&
                            checkOutDate.after(reservation.getCheckInDate())
            ) {
                availableRooms.remove(reservation.getRoom());
            }
        }

        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }

        return customerReservations;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }
}