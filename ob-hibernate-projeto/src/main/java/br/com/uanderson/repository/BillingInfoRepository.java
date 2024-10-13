package br.com.uanderson.repository;

import br.com.uanderson.entities.BillingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Optional
public interface BillingInfoRepository extends JpaRepository<BillingInfo, Long> {
}
