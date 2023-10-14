//Author - Natsuru-san (natsuru-san@mail.com)
//Created 01.10.2023

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import karelia.natsuru.websdr.data.entity.Station;
import karelia.natsuru.websdr.data.repository.StationRepository;
import karelia.natsuru.websdr.data.util.DataBase;

public class QueryTest {

    private DataBase db;

    @BeforeEach
    public void init() {
        db = Mockito.mock(DataBase.class);
    }

    @Test
    public void test() {
        var repo = new StationRepository(null);
        var result = repo.getSaveSql(new Station(
                "CW",
                BigDecimal.valueOf(66),
                BigDecimal.valueOf(77),
                BigDecimal.valueOf(88),
                ZonedDateTime.now(),
                null
                )
        );
        System.out.println(result);
    }
}