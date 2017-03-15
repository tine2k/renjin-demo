package org.tine2k.net.tine2k;

import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.primitives.vector.RowNamesVector;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.*;
import org.renjin.util.DataFrameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Map;

@Component
public class RenjinTest {

    private static final Logger logger = LoggerFactory.getLogger(RenjinTest.class);

    @PostConstruct
    public void runR() {

        logger.info("Lade R...");

        Session session = new SessionBuilder()
                .withDefaultPackages()
                .build();

        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();

        try {
            ListVector eval = (ListVector) engine.eval("a <- read.csv2(text=\"LD;Wert\nAT;8,3\nDE;80\nUS;120\", stringsAsFactors = FALSE);");
            logger.info("" + eval);
            for (NamedValue x : eval.namedValues()) {
                logger.info(x.getName() + "=" + x.getValue().getClass());
                for (Object o : ((Iterable<?>) x.getValue())) {
                    logger.info("" + o);
                }
            }

            engine.put("dp", this);
            logger.info("" + engine.eval("b <- dp$get(\"OS12345\")"));

            logger.info("" + engine.eval("c <- merge(a,b,all=TRUE)"));

            logger.info("" + engine.eval("names(b)"));

            logger.info("" + engine.eval("c[order(-c$Wert),]$LD[1]"));

            logger.info("" + engine.eval("library(compare); comparison <- compare(a,b,allowAll=TRUE); comparison$tM"));


        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }

    public ListVector get(String konzept) {

        ListVector.NamedBuilder builder = new ListVector.NamedBuilder();
        builder.add("LD", new StringArrayVector("FR","GR"));
        builder.add("Wert", new DoubleArrayVector(50,20));
        builder.setAttribute(Symbols.ROW_NAMES, new RowNamesVector(2));
        builder.setAttribute(Symbols.CLASS, StringArrayVector.valueOf("data.frame"));

        return builder.build();
    }

}
