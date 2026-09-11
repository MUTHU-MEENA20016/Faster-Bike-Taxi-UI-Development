// package com.examly.springapp.service;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.examly.springapp.model.Biketaxi;
// import com.examly.springapp.repository.BiketaxiRepo;

// @Service
// public class BiketaxiService {

//     @Autowired
//     private BiketaxiRepo biketaxiRepo;

//     public Biketaxi addBiketaxi(Biketaxi biketaxi) {
//         return biketaxiRepo.save(biketaxi);
//     }

//     public List<Biketaxi> getAllBiketaxi() {
//         return biketaxiRepo.findAll();
//     }

//     public List<Biketaxi> getMyBiketaxi(String username) {
//         return biketaxiRepo.findByUsernameIgnoreCase(username);
//     }
// }
package com.examly.springapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Biketaxi;
import com.examly.springapp.repository.BiketaxiRepo;

@Service
public class BiketaxiService {

    @Autowired
    private BiketaxiRepo biketaxiRepo;

    public Biketaxi addBiketaxi(
            Biketaxi biketaxi) {

        if (biketaxi.getStatus() == null
                || biketaxi.getStatus().trim().isEmpty()) {

            biketaxi.setStatus("PENDING");
        }

        return biketaxiRepo.save(biketaxi);
    }

    public List<Biketaxi> getAllBiketaxi() {

        return biketaxiRepo.findAll();
    }

    public List<Biketaxi> getMyBiketaxi(
            String username) {

        return biketaxiRepo
                .findByUsernameIgnoreCase(username);
    }
}