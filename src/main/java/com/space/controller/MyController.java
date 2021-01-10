package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyController {

    private ShipService service;

    public MyController() {
    }

    @Autowired
    public MyController(ShipService service) {
        this.service = service;
    }

    @GetMapping("/ships")
    public List<Ship> getShipsList(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "planet", required = false) String planet,
                                   @RequestParam(value = "shipType", required = false) ShipType shipType,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                   @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                   @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                   @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                   @RequestParam(value = "minRating", required = false) Double minRating,
                                   @RequestParam(value = "maxRating", required = false) Double maxRating,
                                   @RequestParam(value = "order", required = false) ShipOrder order,
                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        return service.getShipsList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
    }

    @GetMapping("/ships/count")
    public Integer getShipsCount(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "planet", required = false) String planet,
                                 @RequestParam(value = "shipType", required = false) ShipType shipType,
                                 @RequestParam(value = "after", required = false) Long after,
                                 @RequestParam(value = "before", required = false) Long before,
                                 @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                 @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                 @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                 @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                 @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                 @RequestParam(value = "minRating", required = false) Double minRating,
                                 @RequestParam(value = "maxRating", required = false) Double maxRating) {

        return service.getShipsCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
        }
        Ship ship = service.getShip(id);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404
        }
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (service.getShip(id) != null) {
            service.deleteShip(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ship != null
            && ship.getName() != null && isStringValid(ship.getName())
            && ship.getPlanet() != null && isStringValid(ship.getPlanet())
            && ship.getShipType() != null
            && ship.getProdDate() != null && isDateValid(ship.getProdDate())
            && ship.getSpeed() != null && isSpeedValid(ship.getSpeed())
            && ship.getCrewSize() != null && isCrewSizeValid(ship.getCrewSize())) {

            if (ship.getUsed() == null) ship.setUsed(false);
            return new ResponseEntity<>(service.createShip(ship), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id, @RequestBody Ship ship) {
        if (id == null || id <= 0 || ship == null
            || ship.getName() != null && !isStringValid(ship.getName())
            || ship.getPlanet() != null && !isStringValid(ship.getPlanet())
            || ship.getProdDate() != null && !isDateValid(ship.getProdDate())
            || ship.getSpeed() != null && !isSpeedValid(ship.getSpeed())
            || ship.getCrewSize() != null && !isCrewSizeValid(ship.getCrewSize())) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Ship updateShip = service.updateShip(id, ship);

        return updateShip == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updateShip, HttpStatus.OK);
    }

    private boolean isStringValid(String param) {
        return !param.isEmpty() && param.length() <= 50;
    }

    private boolean isSpeedValid(Double speed) {
        double result = new BigDecimal(speed).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return result >= 0.01 && result <= 0.99;

    }

    private boolean isCrewSizeValid(Integer size) {
        return size > 0 && size < 10_000;
    }

    private boolean isDateValid(Date date) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2800);
        Date from = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 3019);
        Date to = calendar2.getTime();

        return date.getTime() > 0 && date.after(from) && date.before(to);
    }


}
