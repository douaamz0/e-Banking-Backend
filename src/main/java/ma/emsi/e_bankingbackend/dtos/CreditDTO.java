package ma.emsi.e_bankingbackend.dtos;


import lombok.Data;

@Data
public class CreditDTO {
    private String accountId;
    private double amount;
    private String description;
}