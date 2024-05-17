package ma.emsi.e_bankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.e_bankingbackend.entities.BankAccount;
import ma.emsi.e_bankingbackend.enums.OperationType;

import java.util.Date;


@Data

public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
