package telran.java58.person.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;
import telran.java58.person.model.Address;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> { //generic 1: specialization type, 2 primary key type

    Stream<Person> findStreamByNameIgnoreCase(String name);

    Person[] findArrayByNameIgnoreCase(String name);

    Stream<Person> findStreamByAddressCityIgnoreCase(String city);
    @Query("select p from Person p where p.address.city=?1")
    Person[] findArrayByAddressCity(String city);

    Person[] findArrayByBirthDateBetween(LocalDate from, LocalDate to);



//    List<Person> findByNameIgnoreCase(String name);
//    List<Person> findByAddress_City(String city);

//    @Query("SELECT p FROM Person p WHERE FUNCTION('TIMESTAMPDIFF', 'YEAR', p.birthDate, CURRENT_DATE) BETWEEN :minAge AND :maxAge")
//    List<Person> findPersonsBetweenAges(@Param("minAge") int minAge, @Param("maxAge") int maxAge);

//@Query("SELECT new telran.java58.person.dto.CityPopulationDto(p.address.city, COUNT(p)) " +
//        "FROM Person p WHERE p.address.city IS NOT NULL " +
//        "GROUP BY p.address.city ORDER BY p.address.city")

//TODO
    // Select city as CITY, count(city) as POPULATION from persons Group By city;

    //@Query("SELECT new telran.java58.person.dto.CityPopulationDto('city', COUNT(p)) " +
//        "FROM Person p JOIN p.address a " +
//        "WHERE 'city' IS NOT NULL " +
//        "GROUP BY 'city' " +
//        "ORDER BY 'city' ")
//@Query("Select 'city' as CITY, count(city) as POPULATION from 'persons' Group By city")
//    @Query(value = "SELECT city AS city, COUNT(city) AS population FROM persons GROUP BY city",
//            nativeQuery = true)
    @Query("select new telran.java58.person.dto.CityPopulationDto(p.address.city, count (p)) from Person p group by p.address.city order by count(p)")
    List<CityPopulationDto> getCitiesPopulation();


}
