package com.kb.catalogInventory.service;

import com.kb.catalogInventory.datatable.QuotesDto;
import com.kb.catalogInventory.model.QuotesBo;
import com.kb.catalogInventory.repository.QuotesRepository;
import com.kb.java.utils.RestApiSuccessResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class QuotesService {

    @Autowired
    private QuotesRepository quotesRepository;

    public Object getRandomQuote(){
        QuotesDto quotesDto = null;
        QuotesBo quotesBo = null;
        RestApiSuccessResponse restApiSuccessResponse = null;

        quotesBo = new QuotesBo();
        quotesDto = quotesRepository.getRandomQuote();
        if(ObjectUtils.isNotEmpty(quotesDto)) {
            BeanUtils.copyProperties(quotesDto, quotesBo);
            restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.OK.value(), "new quote", quotesBo);
        }
        else{
            restApiSuccessResponse = new RestApiSuccessResponse(HttpStatus.NOT_ACCEPTABLE.value(), "new quote", "");
        }
        return restApiSuccessResponse;
    }
}
