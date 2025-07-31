package telran.java58.person.service;

import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;


public interface PersonService {

    void addPerson(PersonDto person);

    PersonDto getPerson(int id);

    PersonDto deletePerson(int id);

    PersonDto updatePersonName(int id, String name);

    PersonDto updatePersonAddress(int id, AddressDto address);

    PersonDto[] findPersonsByName(String name);

    PersonDto[] findPersonsByCity(String city);

    PersonDto[] findPersonsBetweenAges(int minAge, int maxAge);

    Iterable<CityPopulationDto> getCitiesPopulation();

    PersonDto[] findAllChild();

    PersonDto[] findAllEmployeeBySalaryBetween(int minSalary, int maxSalary);

}
