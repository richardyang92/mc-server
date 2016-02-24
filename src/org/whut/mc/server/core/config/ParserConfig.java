package org.whut.mc.server.core.config;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.json.JSONObject;
import org.whut.mc.server.core.communication.Head;
import org.whut.mc.server.core.log.Log;

import java.util.*;

/**
 * Created by yangyang on 16-1-26.
 */
public class ParserConfig extends XMLConfig {
    private static Log log;
    private Map<String, JSONObject> regMap;

    {
        regMap = new HashMap<String, JSONObject>();
    }

    static {
        log = Log.getLogger(ParserConfig.class);
    }

    public ParserConfig(String xmlPath) {
        this.xmlPath = xmlPath;
        try {
            document = getDocument();
        } catch (DocumentException e) {
            log.error("can't load frame config!");
        }
        resolveParserXML();
    }

    private void resolveParserXML() {
        Element frames = getRoot();

        Iterator i = frames.elementIterator();

        while (i.hasNext()) {
            Element node = (Element) i.next();

            if (node.getName().equals(Key.RESOLVER.getKey())) {
                Attribute name = getAttribute(node, Key.NAME.getKey());
                Attribute type = getAttribute(node, Key.TYPE.getKey());
                Attribute regx = getAttribute(node, Key.REGX.getKey());
                Attribute clazz = getAttribute(node, Key.CLASS.getKey());
                JSONObject object = new JSONObject();
                object.put(Key.REGX.getKey(), regx.getValue());
                object.put(Key.CLASS.getKey(), clazz.getValue());
                regMap.put(name.getValue(), object);
            }
        }
    }

    @Override
    public Object get(String key) {
        return getObjFromRegMap(key);
    }

    private Object getObjFromRegMap(String key) {
        JSONObject obj = regMap.get(key);
        if (obj == null) {
            return null;
        }
        return obj;
    }

    public List<JSONObject> getAllResolver() {
        List<JSONObject> allResolver = new ArrayList<JSONObject>();

        Iterator<String> i = regMap.keySet().iterator();

        while (i.hasNext()) {
            String key = i.next();
            allResolver.add(regMap.get(key));
        }
        return allResolver;
    }

    public static void main(String[] args) {
        Config config = new ParserConfig(System.getProperty("user.dir") + "/src/parser.xml");
        JSONObject object = (JSONObject) config.get("lanyan");
        System.out.println(object.get("class"));
    }
}
