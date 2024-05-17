package ma.emsi.e_bankingbackend;

import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import ma.emsi.e_bankingbackend.dtos.BankAccountDTO;
import ma.emsi.e_bankingbackend.dtos.CurrentBankAccountDTO;
import ma.emsi.e_bankingbackend.dtos.CustomerDTO;
import ma.emsi.e_bankingbackend.dtos.SavingBankAccountDTO;
import ma.emsi.e_bankingbackend.entities.*;
import ma.emsi.e_bankingbackend.enums.AccountStatus;
import ma.emsi.e_bankingbackend.enums.OperationType;
import ma.emsi.e_bankingbackend.exceptions.BalanceNotSufficientException;
import ma.emsi.e_bankingbackend.exceptions.BankAccountNotFoundException;
import ma.emsi.e_bankingbackend.exceptions.CustomerNotFoundException;
import ma.emsi.e_bankingbackend.mappers.BankAccountMapperImpl;
import ma.emsi.e_bankingbackend.repositories.AccountOperationRepository;
import ma.emsi.e_bankingbackend.repositories.BankAccountRepository;
import ma.emsi.e_bankingbackend.repositories.CustomerRepository;
import ma.emsi.e_bankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, BankAccountMapperImpl bankAccountMapper){
        return args -> {
            Stream.of("Yassmine","Ilham","Sara").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                CustomerDTO customerDTO=bankAccountMapper.fromCustomer(customer);
                bankAccountService.saveCustomer(customerDTO);

            });
            bankAccountService.listCustomers().forEach(customer->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*150000,5.5,customer.getId());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            });
            List<BankAccountDTO> bankAccounts=bankAccountService.bankAccountList();
            for(BankAccountDTO bankAccount:bankAccounts){
                for(int i=0;i<5;i++){
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO)
                    {
                        accountId=((SavingBankAccountDTO)bankAccount).getId();
                    }else{
                        accountId=((CurrentBankAccountDTO)bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*150000,"credit");
                    bankAccountService.debit(accountId,1000+Math.random()*4000,"debit");

                }
            }
        };
    }
    //@Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository) {

        return args -> {
            BankAccount bankAccount = bankAccountRepository.findById("5ce8b08e-96d7-4411-9fa1-cf2ddc87bbc8").orElse(null);
            if (bankAccount != null) {
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());

                if (bankAccount instanceof CurrentAccount) {
                    System.out.println(((CurrentAccount) bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println(((SavingAccount) bankAccount).getInterestRate());

                }
                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("===============================");
                    System.out.println(op.getType());
                    System.out.println(op.getAmount());
                    System.out.println(op.getOperationDate());
                });
            }
        };

    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){

        return args -> {
            Stream.of("Khadija","Hamid","Safaa").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {

                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(acc->{
                for(int i=0;i<5;i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }
            });




        };
    }




}
