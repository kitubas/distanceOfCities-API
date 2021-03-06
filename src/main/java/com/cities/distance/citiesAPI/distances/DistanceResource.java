package com.cities.distance.citiesAPI.distances;

import com.cities.distance.citiesAPI.distances.service.DistanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/distances")
public class DistanceResource {

    private final DistanceService service;
    Logger log = LoggerFactory.getLogger(DistanceResource.class);

    public DistanceResource(DistanceService service) {
        this.service = service;
    }

    @GetMapping("/by-points")
    public Double byPoints(@RequestParam(name = "from") final Long city1,
                           @RequestParam(name = "to") final Long city2) {
        log.info("byPoints");
        return service.distanceByPointsInMiles(city1, city2);
    }

    @GetMapping("/by-cube")
    public Double byCube(@RequestParam(name = "from") final Long city1,
                         @RequestParam(name = "to") final Long city2) {
        log.info("byCube");
        return service.distanceByCubeInMeters(city1, city2);
    }

    @GetMapping("/by-name")
    public Double distanceByName(@RequestParam(name = "from") final String city1,
                                 @RequestParam(name = "to") final String city2){

        log.info("byName");
        return service.distanceByName(city1, city2);

    }

    /*@GetMapping("/teste-name")
    public String returnTheNames(@RequestParam(name = "from") final String city1,
                                @RequestParam(name = "to") final String city2){

        return service.returnTheNames(city1, city2);
    }*/


    @GetMapping("/closest/{name}")
    public String closest(@PathVariable final String name, Pageable page){
        return service.getClosest(name, page);
    }

}