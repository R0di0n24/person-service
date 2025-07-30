package telran.java58.person.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Embeddable
public class Address {
    private String city;
    private String street;
    private int building;
}
