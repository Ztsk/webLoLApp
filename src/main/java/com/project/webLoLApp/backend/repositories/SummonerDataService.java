package com.project.webLoLApp.backend.repositories;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.project.webLoLApp.WebLoLAppApplication.ApiKey;

@Service
public class SummonerDataService {
    private static final Logger LOGGER = Logger.getLogger(SummonerDataService.class.getName());
    private SummonerDataRepository summonerDataRepository;

    public SummonerDataService(SummonerDataRepository summonerDataRepository){
        this.summonerDataRepository = summonerDataRepository;

    }

    public List<SummonerData> findAll(){
        return summonerDataRepository.findAll();
    }

    public List<SummonerData> findAll(String stringFilter) {
            return summonerDataRepository.search(stringFilter);
    }

    public long count() {
        return summonerDataRepository.count();
    }

    public void delete(SummonerData summonerData) {
        summonerDataRepository.delete(summonerData);
    }

    public void save(SummonerData summonerData){
        if(summonerData == null) {
            LOGGER.log(Level.SEVERE, "SummonerData is null");
            return;
        }
        summonerDataRepository.save(summonerData);
    }

    @PostConstruct
    public void testData(){
        Orianna.setRiotAPIKey(ApiKey);
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        if(summonerDataRepository.count() == 0) {
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("ˉZSK").get()));
        }

    }

}
