package ma.emsi.e_bankingbackend.mappers;

import com.fasterxml.jackson.databind.util.BeanUtil;
import ma.emsi.e_bankingbackend.dtos.AccountOperationDTO;
import ma.emsi.e_bankingbackend.dtos.CurrentBankAccountDTO;
import ma.emsi.e_bankingbackend.dtos.CustomerDTO;
import ma.emsi.e_bankingbackend.dtos.SavingBankAccountDTO;
import ma.emsi.e_bankingbackend.entities.AccountOperation;
import ma.emsi.e_bankingbackend.entities.CurrentAccount;
import ma.emsi.e_bankingbackend.entities.Customer;
import ma.emsi.e_bankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer)
    {
        CustomerDTO customerDTO=new CustomerDTO();
        //cette methode permet de copier les att de customer vers customerDTO
        //on peut aussi procéder par les setters
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){

        SavingBankAccountDTO savingBankAccountDTO= new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingAccount  fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO= new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentAccount fromCurruntBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){

        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){

        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }


}
