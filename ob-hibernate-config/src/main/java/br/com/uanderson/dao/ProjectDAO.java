package br.com.uanderson.dao;

import br.com.uanderson.entities.Project;

import java.util.List;

public interface ProjectDAO {

    List<Project> findAll();
    Project findById(Long id);
    Project create(Project project);
    Project update(Project project);
    boolean deleteById(Long id);
}
