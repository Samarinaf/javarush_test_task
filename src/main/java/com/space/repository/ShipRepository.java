package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query(value = "select ship from Ship ship where " +
            "((:name is null) or ship.name like concat('%', :name, '%')) and " +
            "((:planet is null) or ship.planet like concat('%', :planet, '%')) and " +
            "((:shipType is null) or ship.shipType = :shipType) and " +
            "((:after is null) or ship.prodDate > :after) and " +
            "((:before is null) or ship.prodDate < :before) and " +
            "((:isUsed is null) or ship.isUsed = :isUsed) and " +
            "((:minSpeed is null) or ship.speed >= :minSpeed) and " +
            "((:maxSpeed is null) or ship.speed <= :maxSpeed) and " +
            "((:minCrewSize is null) or ship.crewSize >= :minCrewSize) and " +
            "((:maxCrewSize is null) or ship.crewSize <= :maxCrewSize) and " +
            "((:minRating is null) or ship.rating >= :minRating) and " +
            "((:maxRating is null) or ship.rating <= :maxRating)")
    List<Ship> find(Pageable pageable,
                    @Param("name") String name,
                    @Param("planet") String planet,
                    @Param("shipType") ShipType shipType,
                    @Param("after") Date after,
                    @Param("before") Date before,
                    @Param("isUsed") Boolean isUsed,
                    @Param("minSpeed") Double minSpeed,
                    @Param("maxSpeed") Double maxSpeed,
                    @Param("minCrewSize") Integer minCrewSize,
                    @Param("maxCrewSize") Integer maxCrewSize,
                    @Param("minRating") Double minRating,
                    @Param("maxRating") Double maxRating);

    @Query(value = "select count(ship) from Ship ship where " +
            "((:name is null) or ship.name like concat('%', :name, '%')) and " +
            "((:planet is null) or ship.planet like concat('%', :planet, '%')) and " +
            "((:shipType is null) or ship.shipType = :shipType) and " +
            "((:after is null) or ship.prodDate > :after) and " +
            "((:before is null) or ship.prodDate < :before) and " +
            "((:isUsed is null) or ship.isUsed = :isUsed) and " +
            "((:minSpeed is null) or ship.speed >= :minSpeed) and " +
            "((:maxSpeed is null) or ship.speed <= :maxSpeed) and " +
            "((:minCrewSize is null) or ship.crewSize >= :minCrewSize) and " +
            "((:maxCrewSize is null) or ship.crewSize <= :maxCrewSize) and " +
            "((:minRating is null) or ship.rating >= :minRating) and " +
            "((:maxRating is null) or ship.rating <= :maxRating)")
    Integer getCount(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") ShipType shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating);
}
