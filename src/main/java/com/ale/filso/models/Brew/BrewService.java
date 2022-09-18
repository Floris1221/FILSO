package com.ale.filso.models.Brew;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrewService {

    private BrewRepo brewRepo;

    BrewService(BrewRepo brewRepo){
        this.brewRepo = brewRepo;
    }

    public Brew getBrewById(Integer id){
        return brewRepo.getBrewById(id);
    }

    public List<Brew> getAll(){
        return brewRepo.getAll();
    }

    public Brew update(Brew entity){
        return brewRepo.save(entity);
    }
}
