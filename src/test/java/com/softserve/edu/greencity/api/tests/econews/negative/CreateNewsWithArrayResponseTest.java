package com.softserve.edu.greencity.api.tests.econews.negative;

import com.softserve.edu.greencity.api.assertions.BaseAssertion;
import com.softserve.edu.greencity.api.assertions.ErrorAssertions;
import com.softserve.edu.greencity.api.models.econews.EcoNewsPOSTdto;
import com.softserve.edu.greencity.api.models.errors.PairErrorMessage;
import com.softserve.edu.greencity.api.tests.econews.EcoNewsApiTestRunner;
import com.softserve.edu.greencity.ui.tools.jdbc.entity.EcoNewsEntity;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.softserve.edu.greencity.api.builders.econews.EcoNewsDtoBuilder.ecoNewsDtoWith;
import static com.softserve.edu.greencity.api.data.econews.NewsRepository.*;

public class CreateNewsWithArrayResponseTest extends EcoNewsApiTestRunner {

    @DataProvider(name = "improperNewsWithArrayResponse")
    private Object[] getImproperNewsWithArrayResponse() {
        return new Object[][]{
                //TODO: replace image=null  to proper image data
                {
                        "GC-571",
                        ecoNewsDtoWith().title(null)
                                .text(null)
                                .image(null)
                                .source(null).
                                tags(new String[0]).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("text", "must not be empty"),
                                new PairErrorMessage("title", "must not be empty"),
                                new PairErrorMessage("tags", "must not be empty")
                        }
                },
                {
                        "GC-572",
                        ecoNewsDtoWith().title(null)
                                .text(getMediumText())
                                .image(null)
                                .source(getProperSource())
                                .tags(new String[]{"ads"}).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("title", "must not be empty")
                        }
                },
                {
                        "GC-581",
                        ecoNewsDtoWith().title(getTooLongTitle())
                                .text(getLongText())
                                .image(null)
                                .source(getProperSource())
                                .tags(new String[]{"ads"}).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("title", "size must be between 1 and 170")
                        }
                },
                {
                        "GC-585",
                        ecoNewsDtoWith().title(getShortTitle())
                                .text(getMediumText())
                                .image(null)
                                .source(getProperSource())
                                .tags(new String[0]).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("tags", "must not be empty")
                        }
                },
                {
                        "GC-600",
                        ecoNewsDtoWith().title(getShortTitle())
                                .text(null)
                                .image(null)
                                .source(getProperSource())
                                .tags(new String[]{"ads"}).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("text", "must not be empty")
                        }
                },
                {
                        "GC-601",
                        ecoNewsDtoWith().title(getShortTitle())
                                .text(getTooShortText())
                                .image(null)
                                .source(getProperSource())
                                .tags(new String[]{"ads"}).build(),
                        new PairErrorMessage[] {
                                new PairErrorMessage("text", "size must be between 20 and 63206")
                        }
                }
        };
    }

    @Test(dataProvider = "improperNewsWithArrayResponse", testName = "GC-571, GC-572, GC-581, " +
            "GC-585, GC-600, GC-601")
    public void createNewsWithArrayResponseTest(String testId, EcoNewsPOSTdto ecoNews, PairErrorMessage[] expectedError) {
        logger.info("Running createNewsWithArrayResponseTest: {}", testId);
        Response created = ecoNewsClient.postNews(ecoNews);
        BaseAssertion assertCreated = new BaseAssertion(created);
        ErrorAssertions.arrayEquals(
                created,
                Arrays.asList(expectedError));
        assertCreated.statusCode(400);

        List<EcoNewsEntity> list = ecoNewsService.getNewsByTitle(ecoNews.title);
        Assert.assertTrue(list.isEmpty());
    }
}
