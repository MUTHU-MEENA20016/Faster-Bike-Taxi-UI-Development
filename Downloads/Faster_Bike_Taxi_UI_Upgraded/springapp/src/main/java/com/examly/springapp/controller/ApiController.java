// package com.examly.springapp.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.examly.springapp.model.Biketaxi;
// import com.examly.springapp.service.BiketaxiService;

// @RestController
// public class ApiController {

//     @Autowired
//     private BiketaxiService biketaxiService;

//     @PostMapping("/addBiketaxi")
//     public ResponseEntity<Biketaxi> addBiketaxi(@RequestBody Biketaxi biketaxi) {
//         try {
//             Biketaxi saved = biketaxiService.addBiketaxi(biketaxi);
//             return new ResponseEntity<>(saved, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     // Legacy endpoint retained for the existing test suite and management view.
//     @GetMapping("/getAllBiketaxi")
//     public ResponseEntity<List<Biketaxi>> getAllBiketaxi() {
//         try {
//             List<Biketaxi> list = biketaxiService.getAllBiketaxi();
//             return new ResponseEntity<>(list, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     // Driver dashboard endpoint: derives the username from the JWT, so a driver
//     // cannot request another driver's applications by changing a query parameter.
//     @GetMapping("/myBiketaxi")
//     public ResponseEntity<List<Biketaxi>> getMyBiketaxi(Authentication authentication) {
//         if (authentication == null || authentication.getName() == null) {
//             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//         }
//         return ResponseEntity.ok(biketaxiService.getMyBiketaxi(authentication.getName()));
//     }
// }
package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Biketaxi;
import com.examly.springapp.service.BiketaxiService;

@RestController
public class ApiController {

    @Autowired
    private BiketaxiService biketaxiService;


    @PostMapping("/addBiketaxi")
    public ResponseEntity<Biketaxi> addBiketaxi(
            @RequestBody Biketaxi biketaxi,
            Authentication authentication) {

        try {

            /*
             * If the request has a valid JWT,
             * associate the application with
             * the authenticated username.
             */
            if (authentication != null
                    && authentication.getName() != null) {

                biketaxi.setUsername(
                        authentication.getName()
                );
            }

            if (biketaxi.getStatus() == null
                    || biketaxi.getStatus()
                        .trim()
                        .isEmpty()) {

                biketaxi.setStatus("PENDING");
            }

            Biketaxi saved =
                    biketaxiService.addBiketaxi(
                            biketaxi
                    );

            return new ResponseEntity<>(
                    saved,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            return new ResponseEntity<>(
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    /*
     * Legacy endpoint.
     *
     * Kept because your existing test suite
     * uses /getAllBiketaxi.
     */
    @GetMapping("/getAllBiketaxi")
    public ResponseEntity<List<Biketaxi>>
            getAllBiketaxi() {

        try {

            List<Biketaxi> list =
                    biketaxiService
                        .getAllBiketaxi();

            return new ResponseEntity<>(
                    list,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            return new ResponseEntity<>(
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    /*
     * Driver-specific endpoint.
     *
     * The username comes from the JWT.
     * Therefore a driver cannot simply
     * change a username in the URL to
     * see another driver's applications.
     */
    @GetMapping("/myBiketaxi")
    public ResponseEntity<List<Biketaxi>>
            getMyBiketaxi(
                    Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null) {

            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED
            );
        }

        return ResponseEntity.ok(
                biketaxiService.getMyBiketaxi(
                        authentication.getName()
                )
        );
    }
}