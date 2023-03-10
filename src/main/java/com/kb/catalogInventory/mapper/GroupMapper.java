package com.kb.catalogInventory.mapper;

import com.kb.catalogInventory.datatable.Groups;
import com.kb.catalogInventory.model.GroupsBo;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Log4j2
public class GroupMapper {
   Logger logger= LoggerFactory.getLogger(GroupMapper.class);

public Groups map(GroupsBo groupsBo){

      logger.info("Inside the map of GroupMapper" +groupsBo.getTitle());
     Groups groupDto=new Groups();
     try {
         groupDto.setId(groupsBo.getId());
     }catch (Exception e){

     }
     groupDto.setTitle(groupsBo.getTitle());
      groupDto.setPhoneString(groupsBo.getPhoneString());
      groupDto.setDescription(groupsBo.getDescription());
      groupDto.setCreationDate(groupsBo.getCreationDate());
      groupDto.setAgentId(groupsBo.getAgentId());
      groupDto.setIsActive(groupsBo.getIsActive());

     logger.info("exit from the map of GroupMapper" +groupsBo.getTitle());
      return groupDto;




 }
    public GroupsBo map(Groups groupsDo){

        logger.info("Inside the map of GroupMapper" +groupsDo.getTitle());
        GroupsBo groupbo=new GroupsBo();
        groupbo.setId(groupsDo.getId());
        groupbo.setTitle(groupsDo.getTitle());
        groupbo.setPhoneString(groupsDo.getPhoneString());
        groupbo.setDescription(groupsDo.getDescription());
        groupbo.setCreationDate(groupsDo.getCreationDate());
        groupbo.setAgentId(groupsDo.getAgentId());
        groupbo.setIsActive(groupsDo.getIsActive());
         String[] usercount=new String[0];
        if (groupsDo.getPhoneString()!=null)
          {
           usercount=groupsDo.getPhoneString().split(",");
          }
        groupbo.setUserCount(usercount.length);

        logger.info("exit from the map of GroupMapper" +groupsDo.getTitle());
        return groupbo;




    }
}
