package com.softserve.edu.greencity.api.tests.econews.positive;

import com.softserve.edu.greencity.api.assertions.BaseAssertion;
import com.softserve.edu.greencity.api.models.econews.EcoNewsModel;
import com.softserve.edu.greencity.api.models.econews.EcoNewsPOSTdto;
import com.softserve.edu.greencity.api.tests.econews.EcoNewsApiTestRunner;
import com.softserve.edu.greencity.ui.tools.jdbc.entity.EcoNewsEntity;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.softserve.edu.greencity.api.assertions.EcoNewsAssertion.assertExistence;
import static com.softserve.edu.greencity.api.builders.econews.EcoNewsDtoBuilder.ecoNewsDtoWith;
import static com.softserve.edu.greencity.data.econews.NewsDataStrings.*;

public class CreateNewsWithIntermediateValuesTest extends EcoNewsApiTestRunner {

    @DataProvider(name = "intermediateDataNews")
    private Object[] getIntermediateValues() {
        return new Object[]
                {
                        ecoNewsDtoWith().title(TITLE_EKO_LAVKA.getString())
                                .text(CONTENT_EKO_LAVKA.getString())
                                .image("image/png", IMAGE_ECO_LAVKA.getString())
                                .source(SOURCE_EKO_LAVKA.getString())
                                .tags(new String[]{"news", "ads"}).build()
                };
    }

    /**
     * Use this command to run teest from terminal
     * mvn -Dtest=CreateNewsWithIntermediateValuesTest#createNewsWithIntermediateValuesTest test
     */
    @Test(testName = "GC-625", description = "GC-625", dataProvider = "intermediateDataNews")
    @Description("Verify that news will be created with intermediate number of symbols entered in all fields")
    public void createNewsWithIntermediateValuesTest(EcoNewsPOSTdto news) {
        logger.info("Running createNewsWithIntermediateValuesTest");
        // Initial checking
        List<EcoNewsEntity> list = ecoNewsService.getNewsByTitle(news.title);
        Assert.assertTrue(list.isEmpty());

        Response created = ecoNewsClient.postNews(news);
        BaseAssertion assertCreation = new BaseAssertion(created);
        assertCreation.statusCode(201);

        // Verify the creation
        EcoNewsModel expected = created.as(EcoNewsModel.class);
        assertExistence(expected);

        // Clean-up
        ecoNewsService.deleteNewsById(expected.id);
    }
}
