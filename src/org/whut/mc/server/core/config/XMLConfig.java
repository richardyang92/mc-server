package org.whut.mc.server.core.config;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whut.mc.server.core.log.Log;

import java.io.File;
import java.util.Iterator;

/**
 * Created by yangyang on 2015/11/20.
 */
public abstract class XMLConfig implements Config {
    private static Log log;
    protected String xmlPath;
    protected Document document;

    static {
        log = Log.getLogger(XMLConfig.class);
    }

    protected Document getDocument() throws DocumentException {
        SAXReader sr = new SAXReader();
        return sr.read(new File(xmlPath));
    }

    protected Element getRoot() {
        return document.getRootElement();
    }

    protected Element getElement(Element ef, String name) {
        Iterator ie = ef.elementIterator();

        while (ie.hasNext()) {
            Element e = (Element) ie.next();
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    protected Attribute getAttribute(Element e, String name) {
        Iterator ia = e.attributeIterator();

        while (ia.hasNext()) {
            Attribute a = (Attribute) ia.next();
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

}
