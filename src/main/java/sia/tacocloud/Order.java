package sia.tacocloud;

import lombok.Data;
import org.apache.catalina.LifecycleState;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order {

    private Long id;

    private Date placedAt;

    @NotBlank(message = "Name is required")
    private String deliverName;

    @NotBlank(message = "Street is required")
    private String deliverStreet;

    @NotBlank(message = "City is required")
    private String deliverCity;

    @NotBlank(message = "State is required")
    private String deliverState;

    @NotBlank(message = "Zip code is required")
    private String deliverZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
        message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco tacoSaved) {
        this.tacos.add(tacoSaved);
    }
}
