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
        if (personDto instanceof EmploeeyDto) {
            personRepository.save(modelMapper.map(personDto, Employee.class));
            return;
        }
        if (personDto instanceof ChildDto) {
            personRepository.save(modelMapper.map(personDto, Child.class));
            return;
        }

            personRepository.save(modelMapper.map(personDto, Person.class));

    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmploeeyDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(int id, String name) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(name);
//        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

//    @Override
//    public PersonDto updatePersonAddress(int id, AddressDto address) {
//        return null;
//    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(int id, AddressDto addressDto) {
        if (addressDto == null) {
            throw new AddressIllegalException();
        }
//        person.setAddress(modelMapper.map(addressDto, Address.class));

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
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto[] findPersonsByName(String name) {
        return modelMapper.map(personRepository.findArrayByNameIgnoreCase(name), PersonDto[].class);

//                Arrays.stream(personRepository.findArrayByNameIgnoreCase(name))
//                .map(p -> modelMapper.map(p, PersonDto.class))
//                .toArray(PersonDto[]::new);
    }


    //    @Override
//    @Transactional(readOnly = true)
//    public PersonDto[] findPersonsByName(String name){
//        return personRepository.findStreamByNameIgnoreCase(name)
//                .map(p->modelMapper.map(p,PersonDto.class))
//                .toArray(PersonDto[]::new);
//    }
//    public PersonDto[] findPersonsByName(String name) {
//        List<Person> persons = personRepository.findByNameIgnoreCase(name);
//        return persons.stream()
//                .map(person -> modelMapper.map(person, PersonDto.class))
//                .toArray(PersonDto[]::new);
//    }
    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonsByCity(String city) {

        return personRepository.findStreamByAddressCityIgnoreCase(city)
                .map(p -> modelMapper.map(p, PersonDto.class))
                .toArray(PersonDto[]::new)
                ;
    }
//    @Override
//    public PersonDto[] findPersonsByCity(String city) {
//        List<Person> persons = personRepository.findByAddress_City(city);
//        return persons.stream()
//                .map(p -> modelMapper.map(p, PersonDto.class))
//                .toArray(PersonDto[]::new);
//    }

    @Override
    //TODO
    public PersonDto[] findPersonsBetweenAges(int minAge, int maxAge) {
        LocalDate from = LocalDate.now().minusYears(maxAge);
        LocalDate to = LocalDate.now().minusYears(minAge);
        return modelMapper.map(personRepository.findArrayByBirthDateBetween(from, to), PersonDto[].class);
//        List<Person> persons = personRepository.findAll();
////        List<Person> person = personRepository.findPersonsBetweenAges(minAge, maxAge);
//        return persons.stream()
//                .filter(p -> (p.getBirthDate().isAfter(from) && p.getBirthDate().isBefore(to)))
//                .map(p -> modelMapper.map(p, PersonDto.class))
//                .toArray(PersonDto[]::new);

    }

    //TODO
    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
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
}
