package ma.emsi.e_bankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.e_bankingbackend.entities.AccountOperation;
import ma.emsi.e_bankingbackend.entities.Customer;
import ma.emsi.e_bankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;


@Data

public class SavingBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double InterestRate;
}
