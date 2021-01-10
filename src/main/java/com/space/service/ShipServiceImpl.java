package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShipServiceImpl implements ShipService {

    private ShipRepository repository;

    public ShipServiceImpl() {
    }

    @Autowired
    public ShipServiceImpl(ShipRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ship> getShipsList(String name,
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
                                   Integer pageSize) {

        Sort sort = order == null ? Sort.unsorted() : Sort.by(order.getFieldName());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return repository.find(
                pageable,
                name,
                planet,
                shipType,
                after == null ? null : new Date(after),
                before == null ? null : new Date(before),
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating);
    }

    @Override
    public Ship createShip(Ship ship) {
        calculateAndSetRating(ship);
        return repository.save(ship);
    }

    @Override
    public Ship getShip(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Ship updateShip(Long id, Ship newShip) {
        Ship oldShip = repository.findById(id).orElse(null);

        if (oldShip != null) {
            if (newShip.getName() != null) oldShip.setName(newShip.getName());
            if (newShip.getPlanet() != null) oldShip.setPlanet(newShip.getPlanet());
            if (newShip.getShipType() != null) oldShip.setShipType(newShip.getShipType());
            if (newShip.getProdDate() != null) oldShip.setProdDate(newShip.getProdDate());
            if (newShip.getUsed() != null) oldShip.setUsed(newShip.getUsed());
            if (newShip.getSpeed() != null) oldShip.setSpeed(newShip.getSpeed());
            if (newShip.getCrewSize() != null) oldShip.setCrewSize(newShip.getCrewSize());

            calculateAndSetRating(oldShip);
            repository.save(oldShip);
        }
        return oldShip;
    }

    @Override
    public void deleteShip(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Integer getShipsCount(String name,
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
                                 Double maxRating) {
        return repository.getCount(
                name,
                planet,
                shipType,
                after == null ? null : new Date(after),
                before == null ? null : new Date(before),
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating);
    }

    private void calculateAndSetRating(Ship ship) {
        double k = ship.getUsed() ? 0.5 : 1.0;
        int shipProdYear = ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).getYear();
        double rating = BigDecimal
                .valueOf((ship.getSpeed() * k * 80) / (3019 - shipProdYear + 1))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        ship.setRating(rating);
    }


}
