package com.kb.catalogInventory.service;

import com.kb.catalogInventory.datatable.CategoryAttribute;
import com.kb.catalogInventory.datatable.CategoryAttributeOptions;
import com.kb.catalogInventory.model.CategoryAttributeBo;
import com.kb.catalogInventory.model.CategoryAttributeOptionsBO;
import com.kb.catalogInventory.model.CategoryAttributeRS;
import com.kb.catalogInventory.repository.CategoryAttributeOptionsRepository;
import com.kb.catalogInventory.repository.CategoryAttributeRepository;
import com.kb.catalogInventory.repository.CategoryRepository;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryAttributeService {

    @Autowired
    private CategoryAttributeRepository categoryAttributeRepository;

    @Autowired
    private CategoryAttributeOptionsRepository categoryAttributeOptionsRepository;

    public Object saveCategoryAttributue(CategoryAttributeBo categoryAttributeBo){
        CategoryAttribute categoryAttribute = null;
        if(ObjectUtils.isNotEmpty(categoryAttributeBo.getCategoryId())) {
            categoryAttribute = new CategoryAttribute();
            BeanUtils.copyProperties(categoryAttributeBo, categoryAttribute);
            categoryAttribute = categoryAttributeRepository.save(categoryAttribute);
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute saved successfully", categoryAttribute);
            return  restApiSuccessResponse;
        }
        else{
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category id should not be null", categoryAttribute);
            return  restApiSuccessResponse;
        }
    }

    public Object saveCategoryAttributueOptions(CategoryAttributeBo categoryAttributeBo){
        CategoryAttribute categoryAttribute = null;
        if(ObjectUtils.isNotEmpty(categoryAttributeBo.getCategoryId())) {
            categoryAttribute = new CategoryAttribute();
            BeanUtils.copyProperties(categoryAttributeBo, categoryAttribute);
            categoryAttribute = categoryAttributeRepository.save(categoryAttribute);
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute saved successfully", categoryAttribute);
            return  restApiSuccessResponse;
        }
        else{
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "category id should not be null", categoryAttribute);
            return  restApiSuccessResponse;
        }
    }


    public Object getCategoryAttribute(Integer categoryId){
        List<CategoryAttribute> categoryAttribute = categoryAttributeRepository.findByCategory(categoryId);
        CategoryAttributeRS categoryAttributeRS = null;
        if(!categoryAttribute.isEmpty()){

            List<Integer> attributeIds= new ArrayList<>();
            attributeIds.addAll(categoryAttribute.stream().map(st->st.getId()).collect(Collectors.toList()));
       List<CategoryAttributeOptions>   options=  categoryAttributeOptionsRepository.findOptionsByAttributeIds(attributeIds);
            categoryAttributeRS = new CategoryAttributeRS();
            categoryAttributeRS.setId(categoryAttribute.get(0).getId());
            categoryAttributeRS.setUpdatedOn(categoryAttribute.get(0).getUpdatedOn());
            categoryAttributeRS.setCreatedOn(categoryAttribute.get(0).getCreatedOn());
            Map<String,List<String>> aatToOptopnMap= new HashMap<>();
            options.stream().forEach(op->{
                aatToOptopnMap.put(op.getName(),Arrays.asList(op.getValue().split(",")));
            });
           categoryAttributeRS.setAttributesNames(aatToOptopnMap);
            //categoryAttributeRS.setAddedBy(categoryAttribute.get().getAddedBy());
            categoryAttributeRS.setCategoryId(categoryAttribute.get(0).getCategoryId());
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute found", categoryAttributeRS);
            return  restApiSuccessResponse;
        }
        else{
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "No attribute exists for this category", categoryAttributeRS);
            return  restApiSuccessResponse;
        }
    }

    public Object getCategoryAttributeName(){
        List<CategoryAttribute> categoryAttribute = categoryAttributeRepository.findByCategoryName();
        CategoryAttributeRS categoryAttributeRS = null;
        List<String> categoryAttributenameString = new ArrayList<>();
        if(!categoryAttribute.isEmpty()){

            categoryAttribute.stream().forEach(caf->{
                categoryAttributenameString.add(caf.getAttributeName());
            });
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "category attribute found", categoryAttributenameString);
            return  restApiSuccessResponse;
        }
        else{
            RestApiSuccessResponse restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "No attribute exists for this category", categoryAttributenameString);
            return  restApiSuccessResponse;
        }
    }
}
