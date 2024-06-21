package ma.emsi.e_bankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.e_bankingbackend.dtos.CustomerDTO;
import ma.emsi.e_bankingbackend.entities.Customer;
import ma.emsi.e_bankingbackend.exceptions.CustomerNotFoundException;
import ma.emsi.e_bankingbackend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<CustomerDTO> customers(){
       return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomer(keyword);
    }
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO)
    {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO){
       customerDTO.setId(customerId);
      return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer( @PathVariable  Long customerId){
        bankAccountService.deleteCustomer(customerId);
    }

}
