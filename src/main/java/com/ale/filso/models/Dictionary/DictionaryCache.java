package com.ale.filso.models.Dictionary;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryCache{

    private DictionaryRepo dictionaryRepo;
    private DictionaryGroupRepo dictionaryGroupRepo;

    public DictionaryCache(DictionaryRepo dictionaryRepo, DictionaryGroupRepo dictionaryGroupRepo){
        this.dictionaryRepo = dictionaryRepo;
        this.dictionaryGroupRepo = dictionaryGroupRepo;
    }

    //Cacheable
    @Cacheable("dictionary")
    public List<Dictionary> findByGroup(Integer groupId){
        return dictionaryRepo.findByGroup(groupId);
    }

    @Cacheable("dictionary")
    public Dictionary findById(Integer id){
        return dictionaryRepo.findById(id).orElseGet(Dictionary::new);
    }

    @CachePut("dictionary")
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
