package com.ale.filso.models.Dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Scope("singleton")
@Component
public class DictionaryCache {
    private DictionaryService dictionaryService;

    private LinkedHashMap<Integer, List<Dictionary>> dictionaryMap = new LinkedHashMap<>();

    @Autowired
    public DictionaryCache(DictionaryService dictionaryService) {
        this.dictionaryService=dictionaryService;
        refresh();
    }

    public void refresh() {
        System.out.println("#####################Ten refresh");
        List<Dictionary> dictList=dictionaryService.findAllActive();
        List<Integer> dictGroup = dictList.stream().map(Dictionary::getDictionaryGroup).toList();
//        List<String> distinctDictGroup=dictList.stream()
//                .map(dict -> dict.getDictionaryGroup().).distinct().toList();
        for (Integer dictGroupItem:dictGroup) {
            dictionaryMap.put(dictGroupItem, dictList.stream()
                    .filter(dict -> dict.getDictionaryGroup().equals(dictGroupItem)).toList());
        }
    }

    /**
     * get list of dictionaries for dictionary group
     *
     * @param groupId
     * @return
     */
    public List<Dictionary> getDict(Integer groupId) {
        System.out.println(dictionaryMap);
        return dictionaryMap.get(groupId);
    }

    public Dictionary getDict(Integer groupId, Integer dictId) {
        List<Dictionary> dictionaryList = dictionaryMap.get(groupId);
        if (dictionaryList==null) return null;
        return dictionaryList.stream().filter(o -> o.getId().equals(dictId)).findAny().orElse(null);
    }


    public String getDictName(Integer groupId, Integer dictId) {
        List<Dictionary> dictionaryList = dictionaryMap.get(groupId);
        if (dictionaryList==null) return "";
        return dictionaryList.stream().filter(o -> o.getId().equals(dictId)).map(Dictionary::getName).findAny().orElse("");
    }
}
