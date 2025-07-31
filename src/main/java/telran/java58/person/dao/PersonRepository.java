package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
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

    @Query("select new telran.java58.person.dto.CityPopulationDto(p.address.city, count (p)) from Person p group by p.address.city order by count(p)")
    List<CityPopulationDto> getCitiesPopulation();

    @Query("select c from Child c")
    Child[] findAllPersonTypeChild();

    @Query("select e from Employee e where e.salary between ?1 and ?2")
    Employee[] findEmployeeBySalaryBetween(int from, int to);

}
