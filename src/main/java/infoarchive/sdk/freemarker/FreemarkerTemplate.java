package infoarchive.sdk.freemarker;

import com.opentext.ia.sdk.sip.ContentInfo;
import com.opentext.ia.sdk.sip.FixedHeaderAndFooterTemplate;
import com.sun.xml.internal.bind.api.impl.NameConverter;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wil on 15/07/2018.
 */
public class FreemarkerTemplate<D> extends FixedHeaderAndFooterTemplate<D> {

    private static String DEFAULT_ENCODING = "UTF-8";
    // specific to XML Schemas
    private static final String DEFAULT_DATETIME_FORMAT = "xs";
    private static final String TEMPLATE_NAME = "template";
    private static final String MODEL_VARIABLE = "model";
    private static final String CONTENT_VARIABLE = "content";

    private Template template;

    public FreemarkerTemplate(InputStream header, InputStream footer, InputStream row) {
        this(toString(header), toString(footer), toString(row));
    }

    public FreemarkerTemplate(String header, String footer, String row) {
        super(header, footer);
        Configuration conf = new Configuration(Configuration.VERSION_2_3_28);
        conf.setDefaultEncoding(DEFAULT_ENCODING);
        conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        conf.setLogTemplateExceptions(false);
        conf.setWrapUncheckedExceptions(true);
        conf.setDateTimeFormat(DEFAULT_DATETIME_FORMAT);

        try {
            template = new Template(TEMPLATE_NAME, new StringReader(row), conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeRow(D d, Map<String, ContentInfo> map, PrintWriter printWriter) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put(MODEL_VARIABLE, d);
        model.put(CONTENT_VARIABLE, map);

        try {
            template.process(model, printWriter);
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
