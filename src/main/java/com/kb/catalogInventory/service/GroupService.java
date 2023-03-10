package com.kb.catalogInventory.service;

import com.kb.catalogInventory.datatable.Groups;
import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.mapper.GroupMapper;
import com.kb.catalogInventory.model.GroupsBo;
import com.kb.catalogInventory.repository.GroupsRespository;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.SupplierRepository;
import com.kb.java.utils.RestApiErrorResponse;
import com.kb.java.utils.RestApiSuccessResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Component
@Log4j2
public class GroupService {
    Logger logger = LoggerFactory.getLogger(GroupService.class);
    @Autowired
    GroupsRespository groupsRespository;
    @Autowired
    GroupMapper groupMapper;

    @Autowired
    SupplierService supplierService;

    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    ProductCombinationsRepository productCombinationsRepo;

    public ResponseEntity<?> AddGroups(GroupsBo groupsBo) {
        logger.info("Inside the AddGroups of GroupService" + groupsBo.getTitle());
        Groups groupDo = null;

        try {
            if (groupsBo.getCreationDate() == null) {
                groupsBo.setCreationDate(new Date());
            }
            String[] phoneNo=groupsBo.getPhoneString().split(",");
            String strPattern = "^[0-9]{10}$";

            if (!phoneNo[0].matches(strPattern)){
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), "atleast one phonenumber should be available for creating a group","");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            groupDo = groupMapper.map(groupsBo);
            groupDo = groupsRespository.save(groupDo);
            groupsBo = groupMapper.map(groupDo);

        } catch (Exception e) {
            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), "can't create group", e.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
        groupsRespository.save(groupMapper.map(groupsBo));
        logger.info("before return  the AddGroups of GroupService" + groupsBo.getTitle());
        RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Group is created successfully", groupsBo);

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> ModifyGroups(GroupsBo groupsBo) {
        logger.info("Inside the ModifyGroups of GroupService" + groupsBo.getId());
        try {
            if (groupsBo.getId() != null) {
                if (groupsBo.getCreationDate()==null){
                    groupsBo.setCreationDate(new Date());
                }
                String[] phoneNo=groupsBo.getPhoneString().split(",");
                String strPattern = "^[0-9]{10}$";

                if (!phoneNo[0].matches(strPattern)){
                    RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), "atleast one phonenumber should be available for creating a group","");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                }
                Groups groups = groupMapper.map(groupsBo);
                Optional<Groups> groupDto = groupsRespository.findById(groups.getId());
                if (!groupDto.isPresent()) {
                    logger.error("Not a valid group");
                    RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Not a valid group", "");
                   return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                } else {
                    groups = groupsRespository.save(groups);
                    try {
                        logger.info("Before going removeProductsFromCartAndWishlist phoneString is:{}", groups.getPhoneString());
                        if (!ObjectUtils.isEmpty(groups.getPhoneString())){
                            List<Supplier> supplierList = supplierService.fetchSuppliersByGroupString(String.valueOf(groups.getId()));
                            if (!CollectionUtils.isEmpty(supplierList)){
                                supplierService.removeProductsFromCartAndWishlist(groups.getPhoneString(), supplierList);
                            } else {
                                logger.info("No Supplier found with given groupString:{}", groups.getId());
                            }
                        }
                    } catch (Exception e){
                        log.info("Exception Occurred while removing products from cart and wishlist");
                    }
                    syncSupplierProducts(groups);
                    logger.info("successfully updated group" + groupsBo.getId());
                    RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Group updated successfully", groups);
               return   new ResponseEntity<>(successResponse, HttpStatus.OK);

                }

            } else {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
              return   new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

        } catch (Exception e) {

            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
           return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }


    }

    private void syncSupplierProducts(Groups groups) {
        List<Supplier> supplierList = supplierRepo.findByGroupStringLike(String.valueOf(groups.getId()));
        if(!CollectionUtils.isEmpty(supplierList)) {
            productCombinationsRepo.updateUpdatedOnDateBasedProducts(supplierList);
        }
    }

    public ResponseEntity<?> deactivateGroup(GroupsBo groupsBo) {
        logger.info("Inside the deactivateGroup of GroupService" + groupsBo.getId());
        try {
            if (groupsBo.getId() != null) {
                if (groupsBo.getCreationDate()==null){
                    groupsBo.setCreationDate(new Date());
                }
                Optional<Groups> groupDto = groupsRespository.findById(groupsBo.getId());
                if (!groupDto.isPresent()) {
                    logger.error("Not a valid group");
                    RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Not a valid group", "");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                } else {
                    Groups group = groupDto.get();
                    group.setIsActive(false);
                    group= groupsRespository.save(group);
                    syncSupplierProducts(group);
                    logger.info("successfully deactivateGroup" + groupsBo.getId());
                    RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Group deactivated successfully", group);
                    return   new ResponseEntity<>(successResponse, HttpStatus.OK);

                }

            } else {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
                return   new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

        } catch (Exception e) {

            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }


    }

    public  ResponseEntity<?> searchByKeyword(String name,String state,Integer pageNum,Integer pageSize,String SortOrder)

    {
        //pagination
           if (ObjectUtils.isEmpty(name) || name.equalsIgnoreCase(""))
           {
               RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "group title must require to search group","");
              return new ResponseEntity<>(errorResponse,HttpStatus.OK);
           }
        if (ObjectUtils.isEmpty(state) || name.equalsIgnoreCase(""))
        {
            RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "group state must be require for searching","");
          return   new ResponseEntity<>(errorResponse,HttpStatus.OK);
        }

        Page<Groups> dtoPage = null;
        Page<GroupsBo> groupsList=null;

        if (ObjectUtils.isEmpty(pageNum))
            pageNum = 0;
        if (ObjectUtils.isEmpty(pageSize))
            pageSize = 20;

        Pageable pageable = null;

        if (SortOrder != null && SortOrder.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "creationDate"));
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by("creationDate"));
        }

            if (state.equalsIgnoreCase("Active"))
            {
               try {
                   dtoPage = groupsRespository.findByTitleIsContainingAndIsActive(name, true, pageable);
                   groupsList  = dtoPage.map(entity -> {
                       GroupsBo bo = EntityToDto(entity);
                       return bo;
                   });

               }catch (Exception e){
                   RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "something went wrong try again","");
                   new ResponseEntity<>(errorResponse,HttpStatus.OK);
               }}else {
                try {
                    dtoPage = groupsRespository.findByTitleIsContainingAndIsActive(name, false, pageable);
                    groupsList  = dtoPage.map(entity -> {
                        GroupsBo bo = EntityToDto(entity);
                        return bo;
                    });

                }catch (Exception e){
                    RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "something went wrong try again","");
                  return   new ResponseEntity<>(errorResponse,HttpStatus.OK);
                }
            }


        if (groupsList.getContent().size()>0)
        {
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "get all group successfully", groupsList);
            logger.info("before return  the AddGroups of GroupService" + successResponse);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }else {

            RestApiErrorResponse errorResponse=new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "something went wrong try again","");
           return new ResponseEntity<>(errorResponse,HttpStatus.OK);
        }

    }

    public ResponseEntity<?> getAllGroups() {
        logger.info("Inside the getAllGroups of GroupService");
        List<GroupsBo> groupsBo = new ArrayList<>();
        Map<String, Object> grouplist = new HashMap<>();
        try {

            List<Groups> groups = groupsRespository.findAll();
            groups.forEach(group -> {
                groupsBo.add(groupMapper.map(group));
            });

            grouplist.put("groupList", groupsBo);
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "get all group successfully", grouplist);
            logger.info("before return  the AddGroups of GroupService" + successResponse);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Can't get the group list", e.toString());
            logger.error("before return  the AddGroups of GroupService", errorResponse);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }


    }

    public ResponseEntity<?> getActiveGroup() {
        logger.info("Inside the getActiveGroup of GroupService");
        List<GroupsBo> groupsBo = new ArrayList<>();
        Map<String, Object> grouplist = new HashMap<>();
        try {

            List<Groups> groups = groupsRespository.findByAndIsActive();
            groups.forEach(group -> {
                groupsBo.add(groupMapper.map(group));
            });

            grouplist.put("groupList", groupsBo);
            RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "get all group successfully", grouplist);
            logger.info("before return  the AddGroups of GroupService" + successResponse);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Can't get the group list", e.toString());
            logger.error("before return  the AddGroups of GroupService", errorResponse);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }


    }

    public ResponseEntity<?> getDeleteGroup(GroupsBo groupsBo) {

        logger.info("Inside the deactivateGroup of GroupService" + groupsBo.getId());
        try {
            if (groupsBo.getId() != null) {
                if (groupsBo.getCreationDate()==null){
                    groupsBo.setCreationDate(new Date());
                }
                Optional<Groups> groupDto = groupsRespository.findById(groupsBo.getId());
                if (!groupDto.isPresent()) {
                    logger.error("Not a valid group");
                    RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_FOUND.value(), "Not a valid group", "");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                } else {
                    Groups group = groupDto.get();
                    groupsRespository.delete(group);
                    syncSupplierProducts(group);
                    logger.info("successfully deleted the group" + groupsBo.getId());
                    RestApiSuccessResponse successResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "Group deleted successfully", "");
                    return   new ResponseEntity<>(successResponse, HttpStatus.OK);

                }

            } else {
                RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
                return   new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

        } catch (Exception e) {

            RestApiErrorResponse errorResponse = new RestApiErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Group id is must require for update the group", "");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }



    }

    public GroupsBo EntityToDto(Groups entity){
        GroupsBo bo =  groupMapper.map(entity);

        return bo;
    }
}
