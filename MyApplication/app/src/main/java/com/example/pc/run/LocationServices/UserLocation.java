package com.example.pc.run.LocationServices;


public class UserLocation {
    public String email, campus;

    public UserLocation(String email, String campus) {
        this.email = email;
        this.campus = campus;
    }

    public String toString() {
        String r = email + " " + campus;
        return r;
    }

}