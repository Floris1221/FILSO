package com.ale.filso.models.Dictionary;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {

    private DictionaryRepo dictionaryRepo;
    private DictionaryGroupRepo dictionaryGroupRepo;

    public DictionaryService(DictionaryRepo dictionaryRepo, DictionaryGroupRepo dictionaryGroupRepo){
        this.dictionaryRepo = dictionaryRepo;
        this.dictionaryGroupRepo = dictionaryGroupRepo;
    }

    public List<Dictionary> findAllActive() {
        return dictionaryRepo.findAllActive();
    }

    public List<Dictionary> findByGroup(Integer groupId){
        return dictionaryRepo.findByGroup(groupId);
    }

    public Dictionary findById(Integer id){
        return dictionaryRepo.findById(id).orElseGet(Dictionary::new);
    }


    public Dictionary update(Dictionary dictionary){
        return dictionaryRepo.save(dictionary);
    }



    public List<Dictionary> findByGroupFromDb(Integer groupId){
        return dictionaryRepo.findByGroup(groupId);
    }


    /////DictionaryGroup////
    public List<DictionaryGroup> findAll(){
        return dictionaryGroupRepo.findAll();
    }

    public DictionaryGroup findDictionaryGroupById(Integer id){return dictionaryGroupRepo.findById(id).orElseGet(null);}
}
