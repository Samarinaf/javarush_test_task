package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    List<Ship> getShipsList(String name,
                            String planet,
                            ShipType shipType,
                            Long after,
                            Long before,
                            Boolean isUsed,
                            Double minSpeed,
                            Double maxSpeed,
                            Integer minCrewSize,
                            Integer maxCrewSize,
                            Double minRating,
                            Double maxRating,
                            ShipOrder order,
                            Integer pageNumber,
                            Integer pageSize);
    Ship createShip(Ship ship);
    Ship getShip(Long id);
    Ship updateShip(Long id, Ship newShip);
    void deleteShip(Long id);
    Integer getShipsCount(String name,
                          String planet,
                          ShipType shipType,
                          Long after,
                          Long before,
                          Boolean isUsed,
                          Double minSpeed,
                          Double maxSpeed,
                          Integer minCrewSize,
                          Integer maxCrewSize,
                          Double minRating,
                          Double maxRating);
}
