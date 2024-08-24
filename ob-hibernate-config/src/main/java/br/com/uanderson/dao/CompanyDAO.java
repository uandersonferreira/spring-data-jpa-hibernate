package br.com.uanderson.dao;

import br.com.uanderson.entities.Company;

import java.util.List;

public interface CompanyDAO {

    List<Company> findAll();
    Company findById(Long id);
    Company create(Company company);
    Company update(Company company);
    boolean deleteById(Long id);
}
