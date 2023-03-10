package com.kb.catalogInventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.model.GroupsBo;
import com.kb.catalogInventory.model.csvData;
import com.kb.catalogInventory.service.GroupService;
import com.kb.catalogInventory.util.CsvUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/userGroups")
@Validated
@Log4j2
public class UserGroupController {

    @Autowired
    GroupService groupService;

    @ApiOperation("add a new group")
    @PostMapping(value="/addGroups", produces = MediaType.APPLICATION_JSON)
    public Object addGroups(@RequestParam(value = "data",required = true) String groupdata, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        groupdata=groupdata.replace("\\", "");

        GroupsBo groupsBo=mapper.readValue(groupdata, new TypeReference<GroupsBo>(){});

        log.info("getting info of addGroups {}",groupsBo.getTitle());
        Object obj=null;
        if (file!=null){
             StringBuilder s=new StringBuilder();
             s.append(groupsBo.getPhoneString());
            List<csvData>  phone_string=CsvUtils.read(csvData.class, file.getInputStream());
          int count=0;
          for (csvData data:phone_string){
               if (count<phone_string.size()-1){
                   s.append(data.getPhone_string());
                   s.append(",");
               }else {
                   s.append(data.getPhone_string());
               }

            }
          groupsBo.setPhoneString(s.toString());

        }

        obj=groupService.AddGroups(groupsBo);
        return  obj;
    }


    @PostMapping(value="/uploadcsv", produces = org.springframework.http.MediaType.ALL_VALUE)
    public void uploadSimple(@RequestParam(value = "file", required = true) MultipartFile file){
        try {
            List<csvData>  test=CsvUtils.read(csvData.class, file.getInputStream());
            System.out.println("ddddddddddd"+test.get(0).toString());
        }
        catch (Exception e) {

        }
    }






    @ApiOperation("get all  groups")
    @GetMapping(value="/getAllGroups", produces = MediaType.APPLICATION_JSON)
    public Object getAllGroups(){
        log.info("going to getAllgroups");
        Object obj=null;
         obj=groupService.getAllGroups();
         return obj;
    }

    @ApiOperation("get all active groups")
    @GetMapping(value="/getActiveGroup", produces = MediaType.APPLICATION_JSON)
    public Object getActiveGroup(){
        log.info("going to getActiveGroup");
        Object obj=null;
        obj=groupService.getActiveGroup();
        return obj;
    }


    @ApiOperation("Deleting the groups")
    @DeleteMapping(value="/deleteGroup", produces = MediaType.APPLICATION_JSON)
    public Object deleteGroup(@RequestBody GroupsBo groupsBo){
        log.info("going to deleteGroup");
        Object obj=null;
        obj=groupService.getDeleteGroup(groupsBo );
        return obj;
    }

    @ApiOperation("Editing a  group")
    @PostMapping(value="/modifyGroup", produces = MediaType.APPLICATION_JSON)
    public Object modifyGroup(@RequestParam(value = "data",required = true) String groupdata, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        ObjectMapper mapper =new ObjectMapper();
        groupdata=groupdata.replace("\\", "");

        GroupsBo groupsBo=mapper.readValue(groupdata, new TypeReference<GroupsBo>(){});

        log.info("getting info of addGroups {}",groupsBo.getTitle());
        Object obj=null;
        if (file!=null){
            StringBuilder s=new StringBuilder();
            s.append(groupsBo.getPhoneString());
            List<csvData>  phone_string=CsvUtils.read(csvData.class, file.getInputStream());
            int count=0;
            for (csvData data:phone_string){
                if (count<phone_string.size()-1){
                    s.append(data.getPhone_string());
                    s.append(",");
                }else {
                    s.append(data.getPhone_string());
                }

            }
            groupsBo.setPhoneString(s.toString());

        }

        log.info("getting info of modifyGroup {}",groupsBo.getId());

        obj=groupService.ModifyGroups(groupsBo);
        return obj;

    }

    @ApiOperation("Deactiving a  group")
    @PostMapping(value="/deactivateGroup", produces = MediaType.APPLICATION_JSON)
    public Object deactivateGroup(@RequestBody GroupsBo groupsBo){
        log.info("getting info of deactivateGroup {}",groupsBo.getId());
        Object obj=null;
             obj=groupService.deactivateGroup(groupsBo);
             return obj;

    }


    @ApiOperation("searching a  group by name")
    @GetMapping(value="/groupsearchbyKeyword/{name}/{state}", produces = MediaType.APPLICATION_JSON)
    public Object searchByKeyword(@PathVariable("name") String name,@PathVariable("state") String state,
                                  @RequestParam(value = "pageNum", required = false) @Range(min = 0, message = "Page Number cannot be negative") Integer pageNum,
                                  @RequestParam(value = "pageSize", required = false) @Range(min = 1, message = "Page Size cannot be less than 1") Integer pageSize,
                                  @RequestParam(value = "sortOrder", required = false) @NotNull @NotBlank @Pattern(regexp = "(ASC|DESC|asc|desc)", message = "Sorting Order can ONLY be ASC or DESC") String sortOrder                                  ){
        log.info("getting info of groupsearchbyKeyword {}"+name);
        Object obj=null;
        obj=groupService.searchByKeyword(name,state,pageNum,pageSize,sortOrder);
        return obj;

    }




}
