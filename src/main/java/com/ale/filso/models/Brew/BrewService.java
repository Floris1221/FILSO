package com.ale.filso.models.Brew;

import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrewService {

    private BrewRepo brewRepo;
    private UserAuthorization userAuthorization;

    BrewService(BrewRepo brewRepo, UserAuthorization userAuthorization){
        this.brewRepo = brewRepo;
        this.userAuthorization = userAuthorization;
    }

    public Brew getBrewById(Integer id){
        return brewRepo.getBrewById(id);
    }

    public List<Brew> getAll(String text){
        if (text == null || text.isEmpty()) {
            return brewRepo.getAll();
        } else {
            return brewRepo.search(text);
        }

    }

    public Brew update(Brew entity){
        entity.setUpdatedBy(userAuthorization.getUserLogin());
        return brewRepo.save(entity);
    }
}
