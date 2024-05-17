package ma.emsi.e_bankingbackend.dtos;

import lombok.Data;
import ma.emsi.e_bankingbackend.enums.AccountStatus;

import java.util.Date;


@Data

public class CurrentBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
