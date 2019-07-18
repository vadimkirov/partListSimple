package com.example.partlist.service;

import com.example.partlist.model.PartModel;
import com.example.partlist.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {

    private final MainRepository repository;

    @Autowired
    public PartServiceImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public int computersForAssembly() {
        if(repository.findByRequiredIsTrue().size() == 0){
            return 0;
        }
        int quantity = repository.findByRequiredIsTrue().get(0).getQuantity();
        for  (PartModel item: repository.findByRequiredIsTrue()){
            if(item.getQuantity()< quantity){
                quantity = item.getQuantity();
            }
        }
        return quantity;
    }

}
