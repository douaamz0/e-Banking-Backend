package ma.emsi.e_bankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.e_bankingbackend.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j //ajoute un objet log pour logger les msg (sans declarer l'objet log)
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer ==null)
            throw new CustomerNotFoundException("Customer not found ");
        CurrentAccount bankAccount=new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.fromCurrentBankAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer ==null)
            throw new CustomerNotFoundException("Customer not found ");
        SavingAccount bankAccount=new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCustomer(customer);
        bankAccount.setInterestRate(interestRate);
        SavingAccount savedSavingAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.fromSavingBankAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {

       List<Customer> customers=  customerRepository.findAll();
        List<CustomerDTO> collect = customers.stream().map(cust -> bankAccountMapper.fromCustomer(cust))
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found !")
                );
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found !")
                );
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found !")
                );
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+ accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAccountList()
    {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {

                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
     return   accountOperations.stream().map(op->bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Account not found"));


        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId,PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(op -> bankAccountMapper.fromAccountOperation(op))
                .collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOs(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword) {
        List<Customer> customers=customerRepository.findByNameContains(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> getCustomerAccounts(long customerId) {
        List<BankAccount> bankAccounts=bankAccountRepository.findByCustomer_Id(customerId);
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {

                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;


    }

}
