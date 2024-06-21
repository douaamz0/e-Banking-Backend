package ma.emsi.e_bankingbackend.repositories;

import ma.emsi.e_bankingbackend.entities.BankAccount;
import ma.emsi.e_bankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findByCustomer_Id(long customerId);
}
