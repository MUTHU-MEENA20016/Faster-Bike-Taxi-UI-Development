// package com.examly.springapp.model;

// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;

// @Entity
// public class Biketaxi {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private int id;

//     private String name;
//     private String bikeNumber;
//     private int age;
//     private String phoneNumber;

//     // Username of the authenticated driver who owns this application.
//     // Nullable so existing legacy applications continue to work.
//     private String username;

//     public Biketaxi() {
//     }

//     public Biketaxi(int id, String name, String bikeNumber, int age, String phoneNumber) {
//         this.id = id;
//         this.name = name;
//         this.bikeNumber = bikeNumber;
//         this.age = age;
//         this.phoneNumber = phoneNumber;
//     }

//     public int getId() {
//         return id;
//     }

//     public void setId(int id) {
//         this.id = id;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getBikeNumber() {
//         return bikeNumber;
//     }

//     public void setBikeNumber(String bikeNumber) {
//         this.bikeNumber = bikeNumber;
//     }

//     public int getAge() {
//         return age;
//     }

//     public void setAge(int age) {
//         this.age = age;
//     }

//     public String getPhoneNumber() {
//         return phoneNumber;
//     }

//     public void setPhoneNumber(String phoneNumber) {
//         this.phoneNumber = phoneNumber;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }
// } 
package com.examly.springapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Biketaxi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String bikeNumber;
    private int age;
    private String phoneNumber;

    private String username;

    private String status = "PENDING";

    public Biketaxi() {
    }

    public Biketaxi(
            int id,
            String name,
            String bikeNumber,
            int age,
            String phoneNumber) {

        this.id = id;
        this.name = name;
        this.bikeNumber = bikeNumber;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}