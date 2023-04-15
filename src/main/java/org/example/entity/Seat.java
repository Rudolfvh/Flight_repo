package org.example.entity;


import java.util.Objects;

public class Seat {
    public Seat(Long aircraftId, String seatNo) {
        this.aircraftId = aircraftId;
        this.seatNo = seatNo;
    }

    private Long aircraftId;
    private String seatNo;

    @Override
    public String toString() {
        return "Seat{" +
               "aircraftId=" + aircraftId +
               ", seatNo='" + seatNo + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(aircraftId, seat.aircraftId) && Objects.equals(seatNo, seat.seatNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftId, seatNo);
    }

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}
