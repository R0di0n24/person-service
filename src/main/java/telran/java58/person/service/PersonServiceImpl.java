package telran.java58.person.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.*;
import telran.java58.person.dto.exception.AddressIllegalException;
import telran.java58.person.dto.exception.PersonExistsException;
import telran.java58.person.dto.exception.PersonNotFoundException;
import telran.java58.person.model.Address;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistsException();
        }
        personRepository.save(responseTypeOfEntityDto(personDto));

    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return responseTypeOfEntity(person);
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return responseTypeOfEntity(person);
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(int id, String name) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(name);
//        personRepository.save(person);
        return responseTypeOfEntity(person);
    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(int id, AddressDto addressDto) {
        if (addressDto == null) {
            throw new AddressIllegalException();
        }
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);

        if (addressDto.getCity() != null) {
            person.getAddress().setCity(addressDto.getCity());
        }
        if (addressDto.getStreet() != null) {
            person.getAddress().setStreet(addressDto.getStreet());
        }
        if (addressDto.getBuilding() != null) {  // && addressDto.getBuilding() > 0 maybe needs)
            person.getAddress().setBuilding(addressDto.getBuilding());
        }
//        personRepository.save(person);
        return responseTypeOfEntity(person);
    }

    @Override
    public PersonDto[] findPersonsByName(String name) {
        return Arrays.stream(personRepository.findArrayByNameIgnoreCase(name))
                .map(this::responseTypeOfEntity)
                .toArray(PersonDto[]::new);
        //modelMapper.map(personRepository.findArrayByNameIgnoreCase(name), PersonDto[].class);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonsByCity(String city) {

        return personRepository.findStreamByAddressCityIgnoreCase(city)
                .map(p -> modelMapper.map(p, PersonDto.class))
                .toArray(PersonDto[]::new)
                ;
    }
    @Override
    public PersonDto[] findPersonsBetweenAges(int minAge, int maxAge) {
        LocalDate from = LocalDate.now().minusYears(maxAge);
        LocalDate to = LocalDate.now().minusYears(minAge);
        return Arrays.stream(personRepository.findArrayByBirthDateBetween(from,to))
                .map(this::responseTypeOfEntity)
                .toArray(PersonDto[]::new);

    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
    }

    @Override
    public PersonDto[] findAllChild() {
        return modelMapper.map(personRepository.findAllPersonTypeChild(), ChildDto[].class);
    }

    @Override
    public PersonDto[] findAllEmployeeBySalaryBetween(int minSalary, int maxSalary) {
        return modelMapper.map(personRepository.findEmployeeBySalaryBetween(minSalary, maxSalary), EmployeeDto[].class);
    }

    @Override
    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person = new Person(1000, "John", LocalDate.of(1985, 3, 11),
                    new Address("Tel-Aviv", "Ben Gvirol", 81));
            Child child = new Child(2000, "Peter", LocalDate.of(2019, 7, 5),
                    new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
            Employee emploee = new Employee(3000, "Mary", LocalDate.of(1995, 11, 23),
                    new Address("Rehovot", "Ben Herzl", 7), "Microsoft", 20_000);
            personRepository.saveAll(Arrays.asList(person, child, emploee));

        }
    }

    private Person responseTypeOfEntityDto(PersonDto personDto) {
        if (personDto instanceof ChildDto) {
            return modelMapper.map(personDto, Child.class);
        }
        if (personDto instanceof EmployeeDto) {
            return modelMapper.map(personDto, Employee.class);
        }
        return modelMapper.map(personDto, Person.class);
    }

    private PersonDto responseTypeOfEntity(Person person) {
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }
}
