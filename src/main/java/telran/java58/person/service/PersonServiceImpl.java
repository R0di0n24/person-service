package telran.java58.person.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;
import telran.java58.person.dto.exception.AddressIllegalException;
import telran.java58.person.dto.exception.PersonExistsException;
import telran.java58.person.dto.exception.PersonNotFoundException;
import telran.java58.person.model.Address;
import telran.java58.person.model.Person;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistsException();
        }
        personRepository.save(modelMapper.map(personDto, Person.class));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
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
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

//    @Override
//    public PersonDto updatePersonAddress(int id, AddressDto address) {
//        return null;
//    }

    @Override
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
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto[] findPersonsByName(String name) {
        List<Person> persons = personRepository.findByNameIgnoreCase(name);
        return persons.stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsByCity(String city) {
        List<Person> persons = personRepository.findByAddress_City(city);
        return persons.stream()
                .map(p -> modelMapper.map(p, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    //TODO
    public PersonDto[] findPersonsBetweenAges(int minAge, int maxAge) {
//        List<Person> person = personRepository.findPersonsBetweenAges(minAge, maxAge);
//        return person.stream()
//                .map(p -> modelMapper.map(p,PersonDto.class))
//                .toArray(PersonDto[]::new);
        return null;
    }
//TODO
    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
    }
}
