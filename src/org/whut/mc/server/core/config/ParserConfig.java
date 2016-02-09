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
    private Head head;

    {
        regMap = new HashMap<String, JSONObject>();
        head = new Head();
        head.setStatus(false);
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

    public Head getHead() {
        return head;
    }

    private void resolveParserXML() {
        Element frames = getRoot();

        Iterator i = frames.elementIterator();

        while (i.hasNext()) {
            Element node = (Element) i.next();

            if (node.getName().equals(Key.HEAD.getKey())) {
                head.setStatus(true);
                resolveHeadElement(node);
            } else if (node.getName().equals(Key.RESOLVER.getKey())) {
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

    private void resolveHeadElement(Element headElement) {
        Iterator i = headElement.elementIterator();

        while (i.hasNext()) {
            Element node = (Element) i.next();
            String name = node.getName();
            String value = node.getStringValue();
            switch (name) {
                case Head.APPID :
                    head.setAppId(Byte.parseByte(value));
                    break;
                case Head.TRENTID :
                    head.setTrentId(Byte.parseByte(value));
                    break;
                case Head.FRMID :
                    head.setFrmId(Integer.parseInt(value));
                    break;
                case Head.ENCRYPT :
                    head.setEncrypt(Byte.parseByte(value));
                    break;
                case Head.TOTALLEN :
                    head.setTotalLen(Byte.parseByte(value));
                    break;
                case Head.CS :
                    head.setCs(Byte.parseByte(value));
                    break;
            }
        }
    }

    @Override
    public Object get(String key) {
        if (head.isStatus()) {
            switch (key) {
                case Head.APPID :
                    return head.getAppId();
                case Head.TRENTID :
                    return head.getTrentId();
                case Head.FRMID :
                    return head.getFrmId();
                case Head.ENCRYPT :
                    return head.getEncrypt();
                case Head.TOTALLEN :
                    return head.getTotalLen();
                case Head.CS :
                    return head.getCs();
                default:
                    return getObjFromRegMap(key);
            }
        } else {
            return getObjFromRegMap(key);
        }
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
        System.out.println(config.get("appId"));
    }
}
