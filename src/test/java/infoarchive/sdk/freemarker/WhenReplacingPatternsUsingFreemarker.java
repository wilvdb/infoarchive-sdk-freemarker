package infoarchive.sdk.freemarker;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.opentext.ia.sdk.sip.Template;
import com.opentext.ia.sdk.support.datetime.Dates;
import static org.apache.commons.lang3.RandomStringUtils.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wil on 16/07/2018.
 */
public class WhenReplacingPatternsUsingFreemarker  {

    @Test
    public void shouldReplaceVariables() throws IOException {
        Date date = new Date();
        String name1 = randomAlphabetic(5, 10);
        String value1 = randomAlphabetic(5, 10);
        String name2 = randomAlphabetic(5, 10);
        String value2 = randomAlphabetic(5, 10);
        String name3 = randomAlphabetic(5, 10);
        String prefix = randomAlphabetic(5, 10);
        String infix = randomAlphabetic(5, 10);
        String suffix = randomAlphabetic(5, 10);
        Template<Map<String, Object>> template = new FreemarkerTemplate<Map<String, Object>>(randomAlphabetic(5, 10), randomAlphabetic(5, 10),
                prefix + "${model." + name1 + '}' + infix + "${model." + name2 + '}' + "${model." + name3 + "?datetime}" + suffix);
        Map<String, Object> values = new HashMap<>();
        values.put(name1, value1);
        values.put(name2, value2);
        values.put(name3, date);
        Writer actual = new StringWriter();

        template.writeRow(values, Collections.emptyMap(), new PrintWriter(actual));

        Assert.assertEquals("Text", prefix + value1 + infix + value2  + Dates.toIso(date) + suffix, actual.toString());
    }
}
