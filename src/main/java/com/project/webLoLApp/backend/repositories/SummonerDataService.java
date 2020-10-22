package com.project.webLoLApp.backend.repositories;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.project.webLoLApp.backend.SummonerData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (stringFilter == null || stringFilter.isEmpty()) {
            return summonerDataRepository.findAll();
        } else {
            return summonerDataRepository.search(stringFilter);
        }
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
        Orianna.setRiotAPIKey("RGAPI-b4bc7a0b-a0ca-43b9-91d7-53326bde88fe");
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        if(summonerDataRepository.count() == 0) {
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("ˉZSK").get()));
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("Major BlazeIt").get()));
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("xAvazar").get()));
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("Krzyśˇ").get()));
            summonerDataRepository.save(new SummonerData(Orianna.summonerNamed("Bambo015").get()));
        }
    }

}
