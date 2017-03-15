package org.tine2k.net.tine2k;

import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.NamedValue;
import org.renjin.sexp.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Component
public class RenjinTest {

    private static final Logger logger = LoggerFactory.getLogger(RenjinTest.class);

    @PostConstruct
    public void runR() {

        logger.info("Lade R...");

        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();

        try {
            ListVector eval = (ListVector) engine.eval("read.csv2(text=\"LD;Wert\nAT;8,3\nDE;80\", stringsAsFactors = FALSE);");
            for (NamedValue x : eval.namedValues()) {
                logger.info(x.getName() + "=" + x.getValue().getClass());
                for (Object o : ((Iterable<?>) x.getValue())) {
                    logger.info("" + o);
                }
            }

            engine.put("dp", this);
            logger.info("" + engine.eval("dp$get(\"OS12345\")"));

        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }

    public String get(String konzept) {
        return "myData: " + konzept;
    }

}
