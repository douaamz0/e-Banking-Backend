package ma.emsi.e_bankingbackend.repositories;

import ma.emsi.e_bankingbackend.entities.AccountOperation;
import ma.emsi.e_bankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {


    List<Customer> findByNameContains(String keyword);
}
