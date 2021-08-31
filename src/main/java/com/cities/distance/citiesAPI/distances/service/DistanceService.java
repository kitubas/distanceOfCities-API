package com.cities.distance.citiesAPI.distances.service;

import com.cities.distance.citiesAPI.cities.City;
import com.cities.distance.citiesAPI.cities.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistanceService {

    private final CityRepository cityRepository;
    Logger log = LoggerFactory.getLogger(DistanceService.class);

    public DistanceService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }



    public Double distanceByPointsInMiles(final Long city1, final Long city2) {
        log.info("nativePostgresInMiles({}, {})", city1, city2);
        return cityRepository.distanceByPoints(city1, city2);
    }

    public Double distanceByCubeInMeters(Long city1, Long city2) {
        log.info("distanceByCubeInMeters({}, {})", city1, city2);
        final List<City> cities = cityRepository.findAllById((Arrays.asList(city1, city2)));

        Point p1 = cities.get(0).getLocation();
        Point p2 = cities.get(1).getLocation();

        return cityRepository.distanceByCube(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public Double distanceByName(String city1, String city2) {
        Optional<City> byName1 = cityRepository.findByName(city1);
        Optional<City> byName2 = cityRepository.findByName(city2);

        return distanceByCubeInMeters(byName1.get().getId(), byName2.get().getId());


    }

    public String getClosest(String city1, Pageable page) {
        Optional<City> cityByName = cityRepository.findByName(city1);
        //Point locationCity1 = cityByName.get().getLocation();
        Double menorDistancia = Double.MAX_VALUE;
        String closestCity = "inicio";

        City cidadeNome = cityByName.get();

        //Page<City> all1 = cityRepository.findAll(page);


        //List<City> content = all1.getContent();

        List<Long> allIds = cityRepository.getAllIds();


        //for que demora mt pra carregar
        for (int i = 0; allIds.iterator().hasNext() ; i++) {
            Long next = allIds.iterator().next();
            Double distancia = distanceByCubeInMeters(cidadeNome.getId(),next);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                closestCity = cityRepository.findById(next).get().getName();
                allIds.iterator().remove();

            }
            allIds.iterator().remove();
        }

        //for que pega uma cidade aleatoria
       /*  for (City cidade: content) {
            Double distance = distanceByCubeInMeters(cidade.getId(), cidadeNome.getId());
            if (distance < menorDistancia) {
                menorDistancia = distance;
                closestCity = cidade.getName();
            }
        }*/

        return closestCity;



    }



    /*public String returnTheNames(String city1, String city2) {
        Optional<City> byName1 = cityRepository.findByName(city1);
        Optional<City> byName2 = cityRepository.findByName(city2);

        String s = "começo  ";
        if (byName1.isPresent())
            s = byName1.get().getName();

        if (byName2.isPresent())
            s = byName2.get().getName();

        return s;


    }*/
}