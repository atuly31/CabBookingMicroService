package com.cbs.Ride.Repository;

import com.cbs.Ride.Dto.RideDto;
import com.cbs.Ride.Entity.Rides;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Rides,Long> {

    List<Rides> findByUserId(long id);
    List<Rides> findByDriverId(long id);
}
