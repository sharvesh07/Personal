package com.ticketsearching;

import java.util.Date;

public class TicketRecord {
    private int ticketNo;
    private String passengerName;
    private int trainNo;
    private String onboardStation;
    private String destinationStation;
    private int passengerCount;
    private Date onboardDate;
    private int tripNo;
    private Date bookingDate;
    private String ticketStatus;

    public TicketRecord(int ticketNo, String passengerName, int trainNo, String onboardStation,
                        String destinationStation, int passengerCount, Date onboardDate,
                        int tripNo, Date bookingDate, String ticketStatus) {
        this.ticketNo = ticketNo;
        this.passengerName = passengerName;
        this.trainNo = trainNo;
        this.onboardStation = onboardStation;
        this.destinationStation = destinationStation;
        this.passengerCount = passengerCount;
        this.onboardDate = onboardDate;
        this.tripNo = tripNo;
        this.bookingDate = bookingDate;
        this.ticketStatus = ticketStatus;
    }
    // Getters and setters for all fields
    public int getTicketNo() { return ticketNo; }
    public void setTicketNo(int ticketNo) { this.ticketNo = ticketNo; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public int getTrainNo() { return trainNo; }
    public void setTrainNo(int trainNo) { this.trainNo = trainNo; }
    public String getOnboardStation() { return onboardStation; }
    public void setOnboardStation(String onboardStation) { this.onboardStation = onboardStation; }
    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }
    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }
    public Date getOnboardDate() { return onboardDate; }
    public void setOnboardDate(Date onboardDate) { this.onboardDate = onboardDate; }
    public int getTripNo() { return tripNo; }
    public void setTripNo(int tripNo) { this.tripNo = tripNo; }
    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
    public String getTicketStatus() { return ticketStatus; }
    public void setTicketStatus(String ticketStatus) { this.ticketStatus = ticketStatus; }
}
