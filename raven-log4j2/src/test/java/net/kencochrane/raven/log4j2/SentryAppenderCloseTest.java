package net.kencochrane.raven.log4j2;

import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.connection.Connection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SentryAppenderCloseTest {
    private MockUpErrorHandler mockUpErrorHandler;
    @Injectable
    private Connection mockConnection = null;
    @Injectable
    private Raven mockRaven = null;

    @BeforeMethod
    public void setUp() {
        mockUpErrorHandler = new MockUpErrorHandler();
        new NonStrictExpectations() {{
            mockRaven.getConnection();
            result = mockConnection;
        }};
    }

    @AfterMethod
    public void tearDown() {
        assertThat(mockUpErrorHandler.getErrorCount(), is(0));
    }

    @Test
    public void testCloseNotCalled() throws Exception {
        SentryAppender sentryAppender = new SentryAppender(mockRaven, false);
        sentryAppender.setHandler(mockUpErrorHandler.getMockInstance());

        sentryAppender.stop();

        new Verifications() {{
            mockConnection.close();
            times = 0;
        }};
    }

    @Test
    public void testClose() throws Exception {
        SentryAppender sentryAppender = new SentryAppender(mockRaven, true);
        sentryAppender.setHandler(mockUpErrorHandler.getMockInstance());

        sentryAppender.stop();

        new Verifications() {{
            mockConnection.close();
        }};
    }
}
