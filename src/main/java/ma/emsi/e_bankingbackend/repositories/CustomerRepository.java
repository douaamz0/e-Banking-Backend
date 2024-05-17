package ma.emsi.e_bankingbackend.repositories;

import ma.emsi.e_bankingbackend.entities.AccountOperation;
import ma.emsi.e_bankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
