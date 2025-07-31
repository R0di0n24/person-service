package telran.java58.person.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmploeeyDto extends PersonDto{
    private String company;
    private int salary;
}
